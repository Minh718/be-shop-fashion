package com.shopro.shop1905.dtos.dtosReq;

import java.time.LocalDate;

import com.shopro.shop1905.enums.TypeVoucher;

import lombok.Getter;
import lombok.Setter;

/**
 * Voucher
 */
@Getter
@Setter
public class VoucherDTO {
    private String code;
    private TypeVoucher type;
    private long discount;
    private long minPrice;
    private long maxDiscount;
    private boolean isActive = true;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

}