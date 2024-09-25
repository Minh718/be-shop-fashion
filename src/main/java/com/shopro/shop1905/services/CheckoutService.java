package com.shopro.shop1905.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosReq.CheckoutReq;
import com.shopro.shop1905.dtos.dtosReq.ItemCheckoutReq;
import com.shopro.shop1905.dtos.dtosRes.CheckoutRes;
import com.shopro.shop1905.entities.CartProductSizeColor;
import com.shopro.shop1905.entities.ProductSizeColor;
import com.shopro.shop1905.entities.UserVoucher;
import com.shopro.shop1905.entities.UserVoucherId;
import com.shopro.shop1905.entities.Voucher;
import com.shopro.shop1905.enums.TypeVoucher;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.repositories.CartProductSizeColorRepository;
import com.shopro.shop1905.repositories.ProductRepository;
import com.shopro.shop1905.repositories.ProductSizeColorRepository;
import com.shopro.shop1905.repositories.UserVoucherRepository;
import com.shopro.shop1905.repositories.VoucherRepository;

import lombok.RequiredArgsConstructor;

/**
 * CheckoutService
 */
@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartProductSizeColorRepository cartProductSizeColorRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherService voucherService;
    private final UserVoucherRepository userVoucherRepository;
    private final RedisService redisService;
    private final ProductRepository productRepository;
    private final ProductSizeColorRepository productSizeColorRepository;

    public CheckoutRes checkoutProducts(CheckoutReq checkoutReq) {
        long totalPrice = 0;
        CheckoutRes checkoutRes = new CheckoutRes();
        for (ItemCheckoutReq cps : checkoutReq.getItems()) {
            CartProductSizeColor cpscdb = cartProductSizeColorRepository.findById(cps.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PLEASE_RELOAD_PAGE));
            if (cpscdb.getQuantity() != cps.getQuantity())
                throw new CustomException(ErrorCode.PLEASE_RELOAD_PAGE);

            var availableQuantity = redisService.getKey("productSizeColor:" + cpscdb.getProductSizeColorId());
            ProductSizeColor productSizeColor = productSizeColorRepository
                    .findByIdFetchProductSizeAndFetchProduct(cpscdb.getProductSizeColorId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_SIZE_COLOR_NOT_EXISTED));
            if (availableQuantity == null || (int) availableQuantity < cps.getQuantity())
                throw new CustomException(ErrorCode.PLEASE_RELOAD_PAGE);
            totalPrice += productSizeColor.getProductSize().getProduct().getPrice() *
                    cpscdb.getQuantity();
        }
        checkoutRes.setTotalPrice(totalPrice);
        checkoutRes.setPaymentFee(totalPrice);
        if (checkoutReq.getCode() != null)
            applyVoucher(checkoutReq.getCode(), totalPrice, checkoutRes);
        return checkoutRes;
    }

    private void applyVoucher(String code, long totalPrice, CheckoutRes checkoutRes) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Voucher voucher = voucherService.checkVoucher(code);
        UserVoucherId userVoucherId = new UserVoucherId(userId, voucher.getId());
        UserVoucher userVoucher = userVoucherRepository.findById(userVoucherId)
                .orElseThrow(() -> new CustomException(ErrorCode.VOUCHER_DONT_BELONG_TO_USER));
        if (userVoucher.isUsed()) {
            throw new CustomException(ErrorCode.VOUCHER_IS_USED_ALREADY);
        }
        if (totalPrice < voucher.getMinPrice())
            throw new CustomException(ErrorCode.BAD_REQUEST, "This voucher only apply for voucher with minimum "
                    + voucher.getMinPrice() + "k");
        long discount = 0;
        if (voucher.getType() == TypeVoucher.FIXED) {
            discount = voucher.getDiscount();
        } else if (voucher.getType() == TypeVoucher.PERCENT) {
            discount = totalPrice * voucher.getDiscount() / 100;
            if (discount > voucher.getMaxDiscount())
                discount = voucher.getMaxDiscount();
        }
        checkoutRes.setDiscount(discount);
        checkoutRes.setPaymentFee(totalPrice - discount);

    }

}