package com.shopro.shop1905.dtos.dtosRes;

import java.time.LocalDateTime;

import com.shopro.shop1905.entities.Color;
import com.shopro.shop1905.entities.ProductSize;

import lombok.Builder;
import lombok.Data;

@Data
public class CartProductSizeColorDTO {
    private Long id;
    private int quantity;
    private String updateAt;
    ProductSizeColorDTO productSizeColor;
}
