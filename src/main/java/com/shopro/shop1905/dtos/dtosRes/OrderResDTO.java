package com.shopro.shop1905.dtos.dtosRes;

import java.time.LocalDateTime;
import java.util.Set;

import com.shopro.shop1905.enums.OrderStatus;
import com.shopro.shop1905.enums.PaymentMethod;
import com.shopro.shop1905.enums.PaymentStatus;
import com.shopro.shop1905.enums.ShippingStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Order
 */
@Getter
@Setter
@Builder
public class OrderResDTO {

    private Long id;
    private long totalAmount;
    private Long discount;
    private String shippingAddress;
    private String phone;
    private String fullName;
    private String urlPayment;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;
    private String trackingNumber;
    private String createdAt;

}