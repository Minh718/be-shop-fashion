package com.shopro.shop1905.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.UserInfoToken;
import com.shopro.shop1905.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserController
 */
@RequestMapping("api/user")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/my-info")

    public ApiRes<UserInfoToken> getInfoUser() {
        log.info("Get user info");
        return ApiRes.<UserInfoToken>builder()
                .result(userService.getMyInfo())
                .build();
    }

}