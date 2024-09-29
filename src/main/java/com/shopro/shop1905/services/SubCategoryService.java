package com.shopro.shop1905.services;

import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosReq.SubCategoryAddDTO;
import com.shopro.shop1905.entities.Category;
import com.shopro.shop1905.entities.SubCategory;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.repositories.CategoryRepository;
import com.shopro.shop1905.repositories.SubCategoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * SubCategoryService
 */
@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public Void addSubCategory(SubCategoryAddDTO subCategoryDTO) {
        Category category = categoryRepository.findById(subCategoryDTO.getIdCategory())
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_SUBCATEGORY));

        SubCategory subCategory = SubCategory.builder()
                .name(subCategoryDTO.getName())
                .thump(subCategoryDTO.getThump())
                .category(category)
                .build();
        subCategoryRepository.save(subCategory);
        return null;
    }
}