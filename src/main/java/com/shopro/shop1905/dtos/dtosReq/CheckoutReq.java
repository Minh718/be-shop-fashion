package com.shopro.shop1905.dtos.dtosReq;

import java.util.Set;

import lombok.Data;

/**
 * CheckoutReq
 */
@Data
public class CheckoutReq {

    private String code;
    private Set<ItemCheckoutReq> items;

}