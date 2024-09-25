package com.shopro.shop1905.dtos.dtosRes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.shopro.shop1905.dtos.dtosReq.DetailProductDTO;
import com.shopro.shop1905.dtos.dtosReq.SubCategoryDTO;
import com.shopro.shop1905.entities.Brand;
import com.shopro.shop1905.entities.DetailProduct;

import lombok.Data;

@Data
public class ProductDetailDTO {
    private Long id;
    private String name;
    private int price;
    private int percent;
    private String image;
    private Brand brand;
    // private LocalDateTime createdDate;
    private SubCategoryDTO subCategory;
    private Set<ProductSizeQuantity> productSizes;
    private DetailProductDTO detailProduct;
}
