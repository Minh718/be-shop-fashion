package com.shopro.shop1905.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shopro.shop1905.enums.TypeVoucher;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

/**
 * Voucher
 */
@Entity
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Enumerated(EnumType.STRING)
    private TypeVoucher type;
    private long discount;
    private long minPrice;
    private long maxDiscount;
    private boolean isActive = true;
    private String description;
    private LocalDate startDate;
    private boolean forNewUser = false;
    private LocalDate endDate;
    private LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();

    // @OneToMany(mappedBy = "voucher")
    // private Set<UserVoucher> UserVouchers;

}