package com.shopro.shop1905.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosReq.ProductAddDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailAdmin;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailDTO;
import com.shopro.shop1905.entities.Product;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductDTO productDTO);

    // @Mapping(ignore = true, target = "detailProduct")
    ProductDetailDTO toProductDetailDTO(Product product);

    @Mapping(target = "subCategory", source = "subCategory.name")
    @Mapping(target = "brand", source = "brand.name")
    ProductDetailAdmin toProductDetailAdmin(Product product);

    // @Mapping(target = "publish", source = "isPublish")
    // @Mapping(target = "draft", source = "isDraft")
    // Product toProduct(ProductAddDTO product);

    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ProductDTO toProductDTO(Product product);

    List<ProductDTO> toProductDTOs(List<Product> products);

    // Uncomment and implement if needed
    // CartProductDTO toCartProductDTO(CartProduct cartProduct);
}
