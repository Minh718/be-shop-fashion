package com.shopro.shop1905.entities;

import java.time.LocalDateTime;
import java.util.Set;

import com.shopro.shop1905.enums.OrderStatus;
import com.shopro.shop1905.enums.PaymentMethod;
import com.shopro.shop1905.enums.PaymentStatus;
import com.shopro.shop1905.enums.ShippingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Order
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TblOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long totalAmount;
    private Long discount;
    private Long shippingCost = 0L;
    private String shippingAddress;
    private String phone;
    private String fullName;
    private Voucher voucher = null;
    @Column(columnDefinition = "TEXT")
    private String urlPayment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus = ShippingStatus.NOT_SHIPPED;

    private String trackingNumber;
    private Long shippingFee = 0L;
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<OrderProduct> orderProducts;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

}