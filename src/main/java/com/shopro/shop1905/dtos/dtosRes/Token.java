package com.shopro.shop1905.dtos.dtosRes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    private String accessToken;
    private String refreshToken;
}
