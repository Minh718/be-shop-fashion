package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class ProductSizeColorDTO {

    private Long id;
    private int quantity;

    private ProductSizeDTO productSize;

    private ColorRes color;
}
