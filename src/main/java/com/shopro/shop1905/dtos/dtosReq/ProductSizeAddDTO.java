package com.shopro.shop1905.dtos.dtosReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductSizeAddDTO {

    @NotNull(message = "sizeId is required")
    private Long sizeId;
    @NotNull(message = "productId is required")
    private Long productId;
    @NotNull(message = "quantity is required")
    private int quantity;

}