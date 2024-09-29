package com.shopro.shop1905.dtos.dtosRes;

import java.time.LocalDate;

import com.shopro.shop1905.enums.TypeLogin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * UserInfo
 */
@Getter
@Setter
@Builder
public class UserInfoToken {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String picture;
    private LocalDate dob;
    private TypeLogin typeLogin;
    private String accessToken;
    private String refreshToken;
}