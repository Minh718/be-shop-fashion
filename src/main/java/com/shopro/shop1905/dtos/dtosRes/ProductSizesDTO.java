package com.shopro.shop1905.dtos.dtosRes;

import java.time.LocalDateTime;

import com.shopro.shop1905.entities.SubCategory;

import lombok.Data;

@Data
public class ProductSizesDTO {
    private Long id;
    private String name;
    private int price;
    private int percent;
    private String image;
    private LocalDateTime createdDate;
    private SubCategory subCategory;
}
