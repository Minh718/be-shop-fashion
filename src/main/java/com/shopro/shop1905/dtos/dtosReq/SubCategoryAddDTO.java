package com.shopro.shop1905.dtos.dtosReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubCategoryAddDTO {
    @NotNull(message = "idCategory is required")
    private Long idCategory;
    @NotNull(message = "name is required")
    private String name;
    private String image;
    @NotNull(message = "thump is required")
    private String thump;
}