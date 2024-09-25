package com.shopro.shop1905.dtos.dtosRes;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SizeDTO {
    private Long id;
    @NotNull(message = "Size name cannot be Null")
    private String name;
}
