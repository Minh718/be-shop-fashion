package com.shopro.shop1905.dtos.dtosRes;

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
    private double totalPrice;
    private double paymentFee;
    private double discount = 0;
}