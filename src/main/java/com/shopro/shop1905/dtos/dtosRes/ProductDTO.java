package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private int price;
    private int percent;
    private String image;
    private String createdDate;
    // SubCategoryDTO subCategory;

}
