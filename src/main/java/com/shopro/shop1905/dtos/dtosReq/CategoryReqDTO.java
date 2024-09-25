package com.shopro.shop1905.dtos.dtosReq;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record CategoryReqDTO(@NotNull(message = "Category name cannot be Null") String name, String image,
        List<String> sizes) {
};