package com.shopro.shop1905.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import com.shopro.shop1905.dtos.dtosRes.CartProductSizeColorDTO;
import com.shopro.shop1905.entities.CartProductSizeColor;
import com.shopro.shop1905.entities.ProductSizeColor;
import com.shopro.shop1905.services.RedisService;

@Mapper
public interface CartProductSizeColorMapper {

    CartProductSizeColorMapper INSTANCE = Mappers.getMapper(CartProductSizeColorMapper.class);

    // @Mapping(target = "productSizeColor.quantity", expression =
    // "java(findQuantityOfProductSize(productSizeColor))")
    CartProductSizeColorDTO toCartProductSizeColorDTO(CartProductSizeColor cartProductSizeColor);

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
