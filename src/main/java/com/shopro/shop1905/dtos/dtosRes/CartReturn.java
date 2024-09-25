package com.shopro.shop1905.dtos.dtosRes;

import java.util.Set;

import lombok.Data;

@Data
public class CartReturn {
    private long id;
    private int quantity;
    private Set<CartProductSizeColorDTO> cartProductsizes;
}
