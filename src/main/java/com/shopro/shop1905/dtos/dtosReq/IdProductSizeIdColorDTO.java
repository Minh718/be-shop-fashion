package com.shopro.shop1905.dtos.dtosReq;

import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdProductSizeIdColorDTO {
    @NotNull(message = "idProductSize cannot be Null")
    private long idProductSize;
    @NotNull(message = "idColor cannot be Null")
    private long idColor;
    @NotNull(message = "quantity cannot be Null")
    private int quantity;

}
