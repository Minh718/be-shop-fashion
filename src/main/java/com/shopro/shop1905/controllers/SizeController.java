package com.shopro.shop1905.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeAdmin;
import com.shopro.shop1905.services.SizeService;

import lombok.RequiredArgsConstructor;

/**
 * SizeController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/size")
public class SizeController {
    private final SizeService SizeService;

    @GetMapping("/product/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiRes<List<ProductSizeAdmin>> getAllSizeOfProductForAdmin(@PathVariable Long id) {
        return ApiRes.<List<ProductSizeAdmin>>builder().code(1000).message("Success")
                .result(SizeService.getAllSizeOfProductForAdmin(id)).build();
    }

}