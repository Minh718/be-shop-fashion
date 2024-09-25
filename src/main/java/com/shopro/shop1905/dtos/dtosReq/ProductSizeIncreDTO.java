package com.shopro.shop1905.dtos.dtosReq;

import jakarta.validation.constraints.NotNull;

public record ProductSizeIncreDTO(@NotNull(message = "Product size id is required") Long idProductSize,
        @NotNull(message = "Quantity is required") Long quantity) {
};
