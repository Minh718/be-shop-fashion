package com.shopro.shop1905.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.controllers.SocketController;
import com.shopro.shop1905.dtos.dtosReq.ItemCheckoutReq;
import com.shopro.shop1905.dtos.dtosReq.OrderDTO;
import com.shopro.shop1905.dtos.dtosRes.CheckoutRes;
import com.shopro.shop1905.dtos.dtosRes.DetailOrderDTO;
import com.shopro.shop1905.dtos.dtosRes.OrderNotificationPayload;
import com.shopro.shop1905.dtos.dtosRes.OrderResDTO;
import com.shopro.shop1905.entities.Cart;
import com.shopro.shop1905.entities.CartProductSizeColor;
import com.shopro.shop1905.entities.OrderProduct;
import com.shopro.shop1905.entities.Payment;
import com.shopro.shop1905.entities.Product;
import com.shopro.shop1905.entities.ProductSizeColor;
import com.shopro.shop1905.entities.TblOrder;
import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.enums.OrderStatus;
import com.shopro.shop1905.enums.PaymentMethod;
import com.shopro.shop1905.enums.PaymentStatus;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.mappers.OrderMapper;
import com.shopro.shop1905.repositories.CartProductSizeColorRepository;
import com.shopro.shop1905.repositories.OrderRepository;
import com.shopro.shop1905.repositories.ProductSizeColorRepository;
import com.shopro.shop1905.repositories.UserRepository;
import com.shopro.shop1905.util.DateTimeUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * OrderService
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartProductSizeColorRepository cartProductSizeColorRepository;
    private final CheckoutService checkoutService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final SocketController socketController;
    private final ProductSizeColorRepository productSizeColorRepository;
    @Autowired
    @Lazy
    private OrderService self;

    public DetailOrderDTO getDetailOrder(Long id) {
        TblOrder order = orderRepository.findByIdFetchOrderProductFetchProductSizeColorFetchProductSize(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_SYSTEM));
        return OrderMapper.INSTANCE.toDetailOrderDTO(order);
    }

    @Transactional(rollbackOn = CustomException.class)
    public DetailOrderDTO save(OrderDTO order, HttpServletRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByIdWithCart(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_SYSTEM));
        Cart cart = user.getCart();
        CheckoutRes checkoutRes = checkoutService.checkoutProducts(order.getCheckoutReq());

        validateCheckout(order, checkoutRes);

        Set<OrderProduct> productOrders = new HashSet<>();

        for (ItemCheckoutReq item : order.getCheckoutReq().getItems()) {
            CartProductSizeColor cpsc = cartProductSizeColorRepository.findById(item.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_SYSTEM));
            Long productSizeColorId = cpsc.getProductSizeColorId();

            int retries = 10;
            int expired = 30;
            boolean productLocked = false;

            for (int i = 0; i < retries; i++) {
                productLocked = redisService.setnxProduct("lock_" + productSizeColorId,
                        i, expired);
                if (productLocked) {
                    processProductOrder(userId, productSizeColorId, cpsc, productOrders, cart);
                    redisService.deleteKey("lock_" + productSizeColorId);
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new CustomException(ErrorCode.ERROR_SYSTEM);
                    }
                }
            }

            if (!productLocked) {
                rollbackProductOrders(productOrders);
                throw new CustomException(ErrorCode.ERROR_SYSTEM);
            }
        }
        Payment payment = new Payment();
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setUserId(userId);
        TblOrder newOrder = createNewOrder(order, productOrders,
                user, payment);
        productOrders.forEach(orderProduct -> orderProduct.setOrder(newOrder));
        newOrder.setOrderProducts(productOrders);
        // notify order success for all users in the system
        // self.processNotifyOrder(newOrder, user);
        processNotifyOrder(newOrder, user);
        if (order.getPaymentMethod() != PaymentMethod.VNPAY) {
            newOrder.setOrderStatus(OrderStatus.CONFIRMED);
        } else {
            String urlPayment = paymentService.createVnPayPayment(request,
                    newOrder.getTotalAmount(), newOrder.getId());
            newOrder.setUrlPayment(urlPayment);
        }
        return OrderMapper.INSTANCE.toDetailOrderDTO(newOrder);
    }

    // @Async("notifyOrder")
    private void processNotifyOrder(TblOrder order, User user) {
        String message = String.format(
                "%s customer has placed an order sucessfully. Thank you for %s' purchase!",
                order.getFullName(), order.getFullName());

        // Notify all users, including user details like image
        OrderNotificationPayload payload = new OrderNotificationPayload(
                user.getPicture(),
                message);

        socketController.notifyOrderSuccess(payload);
    }

    public void confirmPaymentOrder(Map<String, String> reqParams) {
        String vnp_SecureHash = reqParams.remove("vnp_SecureHash");
        if (!vnp_SecureHash.equals(paymentService.getVnpSecureHash(reqParams))) {
            throw new CustomException(ErrorCode.ERROR_PAYMENT);
        }
        Long orderId = Long.parseLong(reqParams.get("vnp_OrderInfo"));
        TblOrder order = orderRepository.findById(orderId).orElseThrow();
        String status = reqParams.get("vnp_ResponseCode");
        if (status.equals("00")) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
            Payment payment = order.getPayment();
            payment.setAmount(Long.parseLong(reqParams.get("vnp_Amount")) / 2500000);
            payment.setTransactionID(reqParams.get("vnp_TransactionNo"));
            payment.setPaymentStatus(PaymentStatus.PAID);
            payment.setTranNew(true);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            payment.setCreatedAt(LocalDateTime.parse(reqParams.get("vnp_PayDate"), formatter));
            orderRepository.save(order);
        } else {
            Hibernate.initialize(order.getOrderProducts());
            self.rollbackOnOrderCancellation(order);
        }
    }

    private void validateCheckout(OrderDTO order, CheckoutRes checkoutRes) {
        if (checkoutRes.getDiscount() != order.getCheckoutRes().getDiscount() ||
                checkoutRes.getPaymentFee() != order.getCheckoutRes().getPaymentFee()
                || checkoutRes.getTotalPrice() != order.getCheckoutRes().getTotalPrice()) {
            throw new CustomException(ErrorCode.RECHECKOUT_FAILED);
        }
    }

    private void processProductOrder(String userId, Long productSizeColorId, CartProductSizeColor cpsc,
            Set<OrderProduct> productOrders, Cart cart) {
        int quantityStock = (int) redisService.getKey("productSizeColor:" + productSizeColorId);
        int quantityBuy = cpsc.getQuantity();
        ProductSizeColor productSizeColor = productSizeColorRepository
                .findByIdFetchProductSizeAndFetchProduct(productSizeColorId)
                .orElseThrow();
        Product product = productSizeColor.getProductSize().getProduct();
        if (quantityStock >= quantityBuy) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .productSizeColor(productSizeColor)
                    .quantity(quantityBuy)
                    .price(product.getPrice() * (100 - product.getPercent()) / 100)
                    .name(product.getName())
                    .image(product.getImage())
                    .build();
            productOrders.add(orderProduct);
            redisService.incrementKey("productSizeColor:" + productSizeColor.getId(), -quantityBuy);
            redisService.incrementKey("productSize:" + productSizeColor.getProductSize().getId(), -quantityBuy);
            cartProductSizeColorRepository.delete(cpsc);
        } else {
            rollbackProductOrders(productOrders);
            throw new CustomException(ErrorCode.ERROR_SYSTEM);
        }
    }

    private void rollbackProductOrders(Set<OrderProduct> productOrders) {
        for (OrderProduct orderProduct : productOrders) {
            redisService.incrementKey("productSizeColor:" + orderProduct.getProductSizeColor().getId(),
                    orderProduct.getQuantity());
            redisService.incrementKey("productSize:" + orderProduct.getProductSizeColor().getProductSize().getId(),
                    orderProduct.getQuantity());
        }
    }

    private TblOrder createNewOrder(OrderDTO orderDTO,
            Set<OrderProduct> productOrders, User user, Payment payment) {
        TblOrder order = OrderMapper.INSTANCE.toOrder(orderDTO);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setPayment(payment);
        payment.setOrder(order);
        return orderRepository.save(order);

    }

    public long getNumberPaymentingOrder() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // User user = userRepository.findById(userId).orElseThrow();
        List<TblOrder> orders = orderRepository.findAllByOrderStatusAndUserIdFetchOrderProduct(OrderStatus.PENDING,
                userId);
        long length = orders.size();
        for (TblOrder order : orders) {
            if (hasPaymentTimeElapsed(order)) {
                self.rollbackOnOrderCancellation(order);
                length -= 1;
            }
        }
        return length;

    }

    @Async("rollBackOrder")
    public void rollbackOnOrderCancellation(TblOrder order) {
        Set<OrderProduct> orderProducts = order.getOrderProducts();
        for (OrderProduct orderProduct : orderProducts) {
            ProductSizeColor productSizeColor = orderProduct.getProductSizeColor();
            redisService.incrementKey("productSizeColor:" + productSizeColor.getId(), orderProduct.getQuantity());
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAndCancelPendingOrders() {
        List<TblOrder> orders = orderRepository.findAllByOrderStatusFetchOrderProduct(OrderStatus.PENDING);

        for (TblOrder order : orders) {
            if (hasPaymentTimeElapsed(order)) {
                self.rollbackOnOrderCancellation(order);
            }
        }
    }

    private boolean hasPaymentTimeElapsed(TblOrder order) {
        LocalDateTime now = DateTimeUtil.getCurrentVietnamTime();
        Duration duration = Duration.between(order.getCreatedAt(), now);

        return duration.toMinutes() > 15;
    }

    public Page<OrderResDTO> getOrders(int page, int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // User user = userRepository.findByIdWithCart(userId)
        // .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
        Pageable pageable = PageRequest.of(page, size);
        Page<TblOrder> orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);

        return orders
                .map(OrderMapper.INSTANCE::toOrderResDTO);
    }
}