package com.shopro.shop1905.dtos.dtosReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ColorQuantity {
    @NotNull(message = "Size name cannot be Null")
    private Long idColor;
    private Long quantity;
}
