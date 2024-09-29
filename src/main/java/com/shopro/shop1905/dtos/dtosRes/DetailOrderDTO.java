package com.shopro.shop1905.dtos.dtosRes;

import java.util.Set;

import com.shopro.shop1905.enums.OrderStatus;
import com.shopro.shop1905.enums.ShippingStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class DetailOrderDTO {
    private Long id;
    private double totalAmount;
    private double discount;
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
    PaymentDTO payment;
    Set<OrderProductResDTO> orderProducts;
}
