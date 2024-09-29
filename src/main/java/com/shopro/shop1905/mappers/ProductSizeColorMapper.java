package com.shopro.shop1905.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorBestSelling;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeDTO;
import com.shopro.shop1905.entities.ProductSize;
import com.shopro.shop1905.entities.ProductSizeColor;

@Mapper
public interface ProductSizeColorMapper {
    ProductSizeColorMapper INSTANCE = Mappers.getMapper(ProductSizeColorMapper.class);

    @Mapping(target = "quantity", ignore = true)
    ProductSizeColorDTO toProductSizeColorDTO(ProductSizeColor productSizeColor);

    @Mapping(target = "remain", ignore = true)
    @Mapping(target = "sold", ignore = true)
    ProductSizeColorBestSelling toProductSizeColorBestSelling(ProductSizeColor productSizeColor);

    @Mapping(target = "quantity", ignore = true)
    ProductSizeDTO toProductSizeDTO(ProductSize productSize);
}
