package com.shopro.shop1905.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorBestSelling;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorDTO;
import com.shopro.shop1905.entities.ProductSizeColor;

@Mapper
public interface ProductSizeColorMapper {
    ProductSizeColorMapper INSTANCE = Mappers.getMapper(ProductSizeColorMapper.class);

    ProductSizeColorDTO toProductSizeColorDTO(ProductSizeColor productSizeColor);

    ProductSizeColorBestSelling toProductSizeColorBestSelling(ProductSizeColor productSizeColor);

}
