package com.shopro.shop1905.dtos.dtosRes;

import com.shopro.shop1905.entities.ProductSize;
import com.shopro.shop1905.entities.ProductSizeColor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSellingQuantityDto {
    ProductSizeColor productSizeColor;
    Long quantity;
}
