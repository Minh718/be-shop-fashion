package com.shopro.shop1905.dtos.dtosRes;

import java.util.Set;

import com.shopro.shop1905.entities.Voucher;
import com.shopro.shop1905.enums.OrderStatus;
import com.shopro.shop1905.enums.PaymentMethod;
import com.shopro.shop1905.enums.ShippingStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class DetailOrderDTO {
    private Long id;
    private long totalAmount;
    private Long discount;
    private String shippingAddress;
    private String phone;
    private String fullName;
    private Voucher voucher;
    private String urlPayment;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;
    private String trackingNumber;
    private String createdAt;
    PaymentDTO payment;

    Set<OrderProductResDTO> orderProducts;
}
