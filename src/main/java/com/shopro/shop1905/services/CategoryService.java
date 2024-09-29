package com.shopro.shop1905.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosReq.AddSizeToCategoryReq;
import com.shopro.shop1905.dtos.dtosReq.CategoryReqDTO;
import com.shopro.shop1905.dtos.dtosReq.SizeUpdateDTO;
import com.shopro.shop1905.dtos.dtosReq.UpdateSizeDTO;
import com.shopro.shop1905.dtos.dtosRes.CategoryDTO;
import com.shopro.shop1905.entities.Category;
import com.shopro.shop1905.entities.Size;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.mappers.CategoryMapper;
import com.shopro.shop1905.repositories.CategoryRepository;
import com.shopro.shop1905.repositories.SizeRepository;

import lombok.RequiredArgsConstructor;

/**
 * CategorySer
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SizeRepository sizeRepository;

    public Void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return null;
    }

    public void addSizeToCategory(AddSizeToCategoryReq categorySize) {
        Category category = categoryRepository.findById(categorySize.getIdCategory())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_EXISTED));
        Size size = new Size();
        size.setName(categorySize.getName());
        size.setCategory(category);
        sizeRepository.save(size);
    }

    public void addCategory(CategoryReqDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        List<Size> sizes = categoryDTO.sizes().stream().map(name -> {
            Size size = new Size();
            size.setName(name);
            size.setCategory(category);
            return size;
        }).collect(Collectors.toList());
        category.setSizes(sizes);
        categoryRepository.save(category);
    }

    public void addSizeToCategory(SizeUpdateDTO sizeDTO) {
        Size size = new Size();
        size.setName(sizeDTO.getName());
        sizeRepository.save(size);
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = CategoryMapper.INSTANCE.toListCategoryDTO(categories);
        return categoryDTOs;
    }

    public void updateSizesToCategory(UpdateSizeDTO sizeDTO) {
        Size size = sizeRepository.findById(sizeDTO.getIdSize())
                .orElseThrow(() -> new CustomException(ErrorCode.SIZE_NOT_EXISTED));
        size.setName(sizeDTO.getName());
        sizeRepository.save(size);
    }
}