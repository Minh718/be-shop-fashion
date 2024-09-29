package com.shopro.shop1905.dtos.dtosReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemCheckoutReq {
    @NotNull(message = "idCartProductSize is required")
    private Long id;
    @NotNull(message = "quantity is required")
    private int quantity;
}
