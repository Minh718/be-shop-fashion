package com.shopro.shop1905.dtos.dtosReq;

import lombok.Getter;
import lombok.Setter;

/**
 * dtoRegisterEmail
 */
@Getter
@Setter
public class TokenPasswordRegister {
    private String token;
    private String password;

}