package com.shopro.shop1905.dtos.dtosReq;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetTokenGoogleReq(String code, String clientId, String clientSecret, String redirectUri,
                String grantType) {
}