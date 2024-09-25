package com.shopro.shop1905.controllers;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.SubCategoryAddDTO;
import com.shopro.shop1905.dtos.dtosReq.SubCategoryDTO;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.services.SubCategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/subcategory")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @CacheEvict(value = "getAllCategories", allEntries = true)
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiRes<Void> addSubCategory(@Valid @RequestBody SubCategoryAddDTO subCategory) {
        return ApiRes.<Void>builder().code(1003).message("add subcategory success")
                .result(subCategoryService.addSubCategory(subCategory))
                .build();
    }
}
