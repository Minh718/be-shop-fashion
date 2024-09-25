package com.shopro.shop1905.dtos.dtosRes;

import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * CheckoutRes
 */
@Data
@Setter
@Getter
public class CheckoutRes {
    private long totalPrice;
    private long paymentFee = 0;
    private long discount = 0;
}