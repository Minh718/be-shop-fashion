package com.shopro.shop1905.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.ColorQuantityRes;
import com.shopro.shop1905.dtos.dtosRes.ProductDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailAdmin;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeQuantity;
import com.shopro.shop1905.entities.Product;
import com.shopro.shop1905.entities.ProductDocument;
import com.shopro.shop1905.entities.ProductSize;
import com.shopro.shop1905.entities.ProductSizeColor;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "detailProduct", ignore = true)
    @Mapping(target = "productSizes", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    Product toProduct(ProductDTO productDTO);

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "subCategoryId", source = "subCategory.id")
    @Mapping(target = "thump", source = "subCategory.thump")
    ProductDocument toProductDocument(Product product);

    ProductDTO toProductDTO(ProductDocument product);

    List<ProductDTO> productDocumenttoProductDTOs(List<ProductDocument> productDocuments);

    // @Mapping(ignore = true, target = "detailProduct")
    ProductDetailDTO toProductDetailDTO(Product product);

    @Mapping(target = "subCategory", source = "subCategory.name")
    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "sizes", ignore = true)
    ProductDetailAdmin toProductDetailAdmin(Product product);

    @Mapping(target = "quantity", ignore = true)
    ProductSizeQuantity toProductSizeQuantity(ProductSize productsize);

    @Mapping(target = "quantity", ignore = true)
    ColorQuantityRes toColorQuantityRes(ProductSizeColor productSizeColor);

    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ProductDTO toProductDTO(Product product);

    List<ProductDTO> toProductDTOs(List<Product> products);

    // Uncomment and implement if needed
    // CartProductDTO toCartProductDTO(CartProduct cartProduct);
}
