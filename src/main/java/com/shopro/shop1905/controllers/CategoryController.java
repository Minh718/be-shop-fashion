package com.shopro.shop1905.controllers;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.AddSizeToCategoryReq;
import com.shopro.shop1905.dtos.dtosReq.CategoryReqDTO;
import com.shopro.shop1905.dtos.dtosReq.UpdateSizeDTO;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.CategoryDTO;
import com.shopro.shop1905.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @CacheEvict(value = "getAllCategories", allEntries = true)
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiRes<Void> addCategory(@Valid @RequestBody CategoryReqDTO category) {
        categoryService.addCategory(category);
        return ApiRes.<Void>builder().code(1003).message("add category success")
                .build();
    }

    @CacheEvict(value = "getAllCategories", allEntries = true)
    @PostMapping("/addSize")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiRes<Void> addCategory(@Valid @RequestBody AddSizeToCategoryReq categorySize) {
        categoryService.addSizeToCategory(categorySize);
        return ApiRes.<Void>builder().code(1003).message("add size to category success")
                .build();
    }

    @Cacheable(value = "getAllCategories")
    @GetMapping("/all")
    public ApiRes<List<CategoryDTO>> getAllCategories() {
        return ApiRes.<List<CategoryDTO>>builder().code(1003).message("success")
                .result(categoryService.getAllCategories())
                .build();
    }

    @CacheEvict(value = "getAllCategories", allEntries = true)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiRes<Void> deleteCategory(@PathVariable Long id) {
        return ApiRes.<Void>builder().code(1003).message("delete cactegory is success")
                .result(categoryService.deleteCategory(id))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateSize")
    public ApiRes<String> getSubCategories(@RequestBody UpdateSizeDTO sizeDTO) {
        categoryService.updateSizesToCategory(sizeDTO);
        return ApiRes.<String>builder().code(1003).message("add sizes to category success")
                .build();
    }
}
