package com.shopro.shop1905.dtos.dtosRes;

import java.util.List;

import com.shopro.shop1905.dtos.dtosReq.DetailProductDTO;

import lombok.Data;

@Data
public class ProductDetailAdmin {
    private Long id;
    private String name;
    private int price;
    private int percent;
    private String subCategory;
    private String brand;
    // private String image;
    // private LocalDateTime createdDate;
    // private SubCategoryDTO subCategory;
    private DetailProductDTO detailProduct;
    private List<ProductSizeAdmin> sizes;
}
