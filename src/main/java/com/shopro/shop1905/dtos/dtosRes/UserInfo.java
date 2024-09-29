package com.shopro.shop1905.dtos.dtosRes;

import java.time.LocalDate;
import java.util.Set;

import com.shopro.shop1905.entities.Role;
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
public class UserInfo {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private TypeLogin typeLogin;
    private LocalDate dob;
    private Set<Role> roles;
}