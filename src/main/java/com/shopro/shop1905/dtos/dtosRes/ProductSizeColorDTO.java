package com.shopro.shop1905.dtos.dtosRes;

import com.shopro.shop1905.entities.Color;

import lombok.Data;

@Data
public class ProductSizeColorDTO {

    private Long id;
    private int quantity;

    private ProductSizeDTO productSize;

    private ColorRes color;
}
