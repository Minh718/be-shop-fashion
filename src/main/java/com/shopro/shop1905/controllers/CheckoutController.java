package com.shopro.shop1905.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.CheckoutReq;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.CheckoutRes;
import com.shopro.shop1905.services.CheckoutService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * CheckoutController
 */
@RestController
@RequestMapping("api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping("/products")
    public ApiRes<CheckoutRes> checkoutProducts(@Valid @RequestBody CheckoutReq checkoutReq) {
        return ApiRes.<CheckoutRes>builder().code(1000).message("Success")
                .result(checkoutService.checkoutProducts(checkoutReq)).build();
    }

}