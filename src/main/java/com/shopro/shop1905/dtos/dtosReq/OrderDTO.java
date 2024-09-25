package com.shopro.shop1905.dtos.dtosReq;

import com.shopro.shop1905.dtos.dtosRes.CheckoutRes;
import com.shopro.shop1905.enums.PaymentMethod;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

/**
 * Order
 */
@Getter
@Setter
public class OrderDTO {

    private String shippingAddress;

    private String phone;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private CheckoutRes checkoutRes;
    private CheckoutReq checkoutReq;

}