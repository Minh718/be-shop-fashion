package com.shopro.shop1905.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.CartProductSizeColorDTO;
import com.shopro.shop1905.entities.CartProductSizeColor;

@Mapper
public interface CartProductSizeColorMapper {

    CartProductSizeColorMapper INSTANCE = Mappers.getMapper(CartProductSizeColorMapper.class);

    // @Mapping(target = "productSizeColor.quantity", expression =
    // "java(findQuantityOfProductSize(productSizeColor))")
    @Mapping(target = "productSizeColor", ignore = true)
    CartProductSizeColorDTO toCartProductSizeColorDTO(CartProductSizeColor cartProductSizeColor);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "productSizeColorId", ignore = true)
    CartProductSizeColor toCartProductSizeColor(CartProductSizeColorDTO cartProductSizeColorDTO);

    List<CartProductSizeColorDTO> toCartProductSizeColorDTOs(
            List<CartProductSizeColor> cartProductSizeColors);

    // ProductSizeDTO toProductSizeDTO(ProductSize productSize);

    // protected long findQuantityOfProductSize(ProductSizeColor productSizeColor) {
    // var quantity = redisService.getKey("productSizeColor:" +
    // productSizeColor.getId());
    // return quantity == null ? 0 : (long) quantity;
    // }
}
