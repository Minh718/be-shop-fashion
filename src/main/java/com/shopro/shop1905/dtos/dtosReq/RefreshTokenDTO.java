package com.shopro.shop1905.dtos.dtosReq;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenDTO(@NotNull String refreshToken) {
}