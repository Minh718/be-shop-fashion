package com.shopro.shop1905.dtos.dtosRes;

import java.util.Set;

import com.shopro.shop1905.dtos.dtosReq.SubCategoryDTO;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String image;
    private Set<SubCategoryDTO> subCategories;
}
