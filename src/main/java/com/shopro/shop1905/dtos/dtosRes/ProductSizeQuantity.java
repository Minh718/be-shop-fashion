package com.shopro.shop1905.dtos.dtosRes;

import java.util.List;

import lombok.Data;

@Data
public class ProductSizeQuantity {
    private Long id;
    private int quantity;
    private SizeDTO size;
    private List<ColorQuantityRes> productSizeColors;
}
