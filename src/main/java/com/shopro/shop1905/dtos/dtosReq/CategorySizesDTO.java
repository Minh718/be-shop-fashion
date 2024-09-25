package com.shopro.shop1905.dtos.dtosReq;

import java.util.Set;

import com.shopro.shop1905.entities.Size;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySizesDTO {
    @NotNull(message = "Category id cannot be Null")
    private Long id;
    private Set<SizeUpdateDTO> sizes;
}
