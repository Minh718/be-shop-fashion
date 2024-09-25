package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class ProductSizeDTO {
    private Long id;
    private int quantity;
    private SizeDTO size;
    private ProductDTO product;
}
