package com.shopro.shop1905.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.RefreshTokenDTO;
import com.shopro.shop1905.dtos.dtosReq.UserLogin;
import com.shopro.shop1905.dtos.dtosReq.UserRegisterEmail;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.Token;
import com.shopro.shop1905.dtos.dtosRes.UserInfoToken;
import com.shopro.shop1905.services.AuthService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("api/auth")
@Validated
public class AuthController {
        @Autowired
        private AuthService authService;

        @PostMapping("/register/email")
        public ResponseEntity<ApiRes<Void>> RegisterUserByEmail(@Valid @RequestBody UserRegisterEmail userRegisterEmail)
                        throws IOException {
                authService.RegisterUserByEmail(userRegisterEmail);

                return ResponseEntity.ok().body(
                                ApiRes.<Void>builder().code(1000)
                                                .message("Please check email to complete your registration").build());
        }

        @GetMapping("/register")
        public ResponseEntity<ApiRes<Void>> completeRegister(
                        @NotNull(message = "Token cannot be null") @RequestParam(value = "token") String token) {
                authService.completeRegister(token);
                return ResponseEntity.ok().body(
                                ApiRes.<Void>builder().code(1000)
                                                .message("Register successfully. Login to experience the service")
                                                .build());

        }

        @PostMapping("/login")
        public ResponseEntity<ApiRes<UserInfoToken>> UserLoginByEmail(@Valid @RequestBody UserLogin userLogin) {
                ApiRes<UserInfoToken> res = ApiRes.<UserInfoToken>builder()
                                .code(1000)
                                .message("đăng nhập thành công")
                                .result(authService.UserLoginByEmail(userLogin))
                                .build();
                return ResponseEntity.ok().body(res);
        }

        @PostMapping("/login/google")
        public ApiRes<UserInfoToken> UserLoginByGoogle(
                        @NotNull(message = "code is requried") @RequestParam() String code) {
                return ApiRes.<UserInfoToken>builder()
                                .code(1000)
                                .message("đăng nhập thành công")
                                .result(authService.UserLoginByGoogle(code))
                                .build();
        }

        @PostMapping("/refreshtoken")
        public ApiRes<Token> refreshtoken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
                return ApiRes.<Token>builder()
                                .code(1000)
                                .message("Fresh token successfully")
                                .result(authService.refreshTokenUser(refreshTokenDTO))
                                .build();
        }
}