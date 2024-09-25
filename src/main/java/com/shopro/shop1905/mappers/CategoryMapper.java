package com.shopro.shop1905.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.CategoryDTO;
import com.shopro.shop1905.entities.Category;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toCategoryDTO(Category category);

    List<CategoryDTO> toListCategoryDTO(List<Category> category);

    // CartProductDTO toCartProductDTO(CartProduct cartProduct);
}
