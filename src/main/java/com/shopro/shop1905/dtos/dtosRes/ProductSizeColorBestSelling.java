package com.shopro.shop1905.dtos.dtosRes;

import com.shopro.shop1905.entities.Color;
import com.shopro.shop1905.entities.Size;

import lombok.Data;

@Data
/**
 * ProductSizeBestSelling
 */
public class ProductSizeColorBestSelling {
    Long id;
    ProductSizeDTO productSize;
    Color color;
    long remain;
    Long sold;
}