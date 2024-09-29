package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class CartProductSizeColorDTO {
    private Long id;
    private int quantity;
    private String updateAt;
    ProductSizeColorDTO productSizeColor;
}
