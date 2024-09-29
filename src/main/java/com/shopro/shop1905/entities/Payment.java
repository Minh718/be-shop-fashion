package com.shopro.shop1905.entities;

import java.time.LocalDateTime;

import com.shopro.shop1905.enums.PaymentMethod;
import com.shopro.shop1905.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private long amount;
    private long discount = 0L;
    private long fee = 0L;
    private String transactionID;
    private String userId;
    private boolean tranNew = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @OneToOne
    @MapsId // This tells JPA to use the Order's ID
    @JoinColumn(name = "id")
    private TblOrder order;
}
