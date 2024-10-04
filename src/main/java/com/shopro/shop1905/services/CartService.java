package com.shopro.shop1905.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosReq.CartAddProductDTO;
import com.shopro.shop1905.dtos.dtosRes.CartProductSizeColorDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorDTO;
import com.shopro.shop1905.entities.Cart;
import com.shopro.shop1905.entities.CartProductSizeColor;
import com.shopro.shop1905.entities.ProductSizeColor;
import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.mappers.CartProductSizeColorMapper;
import com.shopro.shop1905.mappers.ProductSizeColorMapper;
import com.shopro.shop1905.repositories.CartProductSizeColorRepository;
import com.shopro.shop1905.repositories.ProductSizeColorRepository;
import com.shopro.shop1905.repositories.UserRepository;
import com.shopro.shop1905.util.DateTimeUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
        private final UserRepository userRepository;
        private final RedisService redisService;
        private final ProductSizeColorRepository productSizeColorRepository;
        private final CartProductSizeColorRepository cartProductSizeColorRepository;

        public Void addProductToCart(CartAddProductDTO cartAddProductDTO) {
                String userId = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByIdWithCart(userId)
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
                Cart cart = user.getCart();

                int requestedQuantity = cartAddProductDTO.getQuantity();

                ProductSizeColor productSizeColor = productSizeColorRepository
                                .findById(cartAddProductDTO.getProductSizeColorId())
                                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));

                var availableQuantity = redisService
                                .getKey("productSizeColor:" + cartAddProductDTO.getProductSizeColorId());

                if (availableQuantity == null) {
                        redisService.setKey("productSizeColor:" +
                                        cartAddProductDTO.getProductSizeColorId(), 0);
                        throw new CustomException(ErrorCode.QUANTITY_NOT_ENOUGH);
                } else if ((int) availableQuantity < requestedQuantity) {
                        throw new CustomException(ErrorCode.QUANTITY_NOT_ENOUGH);
                }
                CartProductSizeColor cartProductSizeColor = cartProductSizeColorRepository
                                .findByCartIdAndProductSizeColorId(cart.getId(),
                                                cartAddProductDTO.getProductSizeColorId())
                                .orElseGet(() -> {
                                        return CartProductSizeColor.builder().cart(cart)
                                                        .productSizeColorId(productSizeColor.getId())
                                                        .build();
                                });
                cartProductSizeColor.setQuantity(requestedQuantity);
                cartProductSizeColor.setUpdateAt(DateTimeUtil.getCurrentVietnamTime());
                cartProductSizeColorRepository.save(cartProductSizeColor);
                return null;
        }

        public Void removeProductFromCart(long cartProductSizeColorId) {
                String userId = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByIdWithCart(userId)
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
                Cart cart = user.getCart();
                CartProductSizeColor cartProductSizeColor = cartProductSizeColorRepository
                                .findByIdAndCartId(cartProductSizeColorId, cart.getId())
                                .orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_SIZE_NOT_EXISTED));
                cartProductSizeColorRepository.delete(cartProductSizeColor);
                cartProductSizeColorRepository.deleteById(cartProductSizeColorId);
                return null;
        }

        public Page<CartProductSizeColorDTO> getAllProductOfCart(int page, int size) {
                String userId = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByIdWithCart(userId)
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
                Pageable pageable = PageRequest.of(page, size);
                Page<CartProductSizeColor> cartProductSizeColors = cartProductSizeColorRepository
                                .findAllByCartIdOrderByUpdateAtDesc(
                                                user.getCart().getId(), pageable);
                List<CartProductSizeColorDTO> dtoList = new ArrayList<>();
                for (CartProductSizeColor cartProductSizeColor : cartProductSizeColors) {
                        var availableQuantity = redisService
                                        .getKey("productSizeColor:" + cartProductSizeColor.getProductSizeColorId());
                        if (availableQuantity == null || (int) availableQuantity <= 0) {
                                cartProductSizeColorRepository.delete(cartProductSizeColor);
                                continue;
                        } else if (cartProductSizeColor.getQuantity() > (int) availableQuantity) {
                                cartProductSizeColor.setQuantity((int) availableQuantity);
                                cartProductSizeColorRepository.save(cartProductSizeColor);
                        }
                        CartProductSizeColorDTO dto = CartProductSizeColorMapper.INSTANCE
                                        .toCartProductSizeColorDTO(cartProductSizeColor);
                        ProductSizeColor productSizeColor = productSizeColorRepository
                                        .findByIdFetchProductSizeAndFetchProduct(
                                                        cartProductSizeColor.getProductSizeColorId())
                                        .orElseThrow(() -> new CustomException(
                                                        ErrorCode.PRODUCT_SIZE_COLOR_NOT_EXISTED));
                        ProductSizeColorDTO productSizeColorDTO = ProductSizeColorMapper.INSTANCE
                                        .toProductSizeColorDTO(productSizeColor);
                        productSizeColorDTO.setQuantity((int) availableQuantity);
                        int quantityProductSize = (int) redisService.getKey(
                                        "productSize:" + productSizeColor.getProductSize().getId());
                        productSizeColorDTO.getProductSize().setQuantity(quantityProductSize);
                        dto.setProductSizeColor(productSizeColorDTO);
                        dtoList.add(dto);
                }

                return new PageImpl<>(dtoList, pageable,
                                cartProductSizeColors.getTotalElements());
        }
}
