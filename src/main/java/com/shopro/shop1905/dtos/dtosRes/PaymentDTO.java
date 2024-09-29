package com.shopro.shop1905.dtos.dtosRes;

import com.shopro.shop1905.enums.PaymentMethod;
import com.shopro.shop1905.enums.PaymentStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private PaymentMethod paymentMethod;
    private long amount;
    private String transactionID;
    private String createdAt;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
