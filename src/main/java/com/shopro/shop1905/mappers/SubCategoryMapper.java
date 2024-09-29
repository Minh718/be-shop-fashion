package com.shopro.shop1905.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosReq.SubCategoryDTO;
import com.shopro.shop1905.entities.SubCategory;

@Mapper
public interface SubCategoryMapper {
    SubCategoryMapper INSTANCE = Mappers.getMapper(SubCategoryMapper.class);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "products", ignore = true)
    SubCategory toSubCategory(SubCategoryDTO subCategoryDTO);

    SubCategoryDTO toSubCategoryDTO(SubCategory subCategory);

    // CartProductDTO toCartProductDTO(CartProduct cartProduct);
}
