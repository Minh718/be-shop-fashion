package com.shopro.shop1905.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.ProductSizeAddDTO;
import com.shopro.shop1905.dtos.dtosReq.ProductSizeIncreDTO;
import com.shopro.shop1905.dtos.dtosRes.ApiMetaRes;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.MetadataDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorBestSelling;
import com.shopro.shop1905.services.ProductSizeColorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * ProductSizeController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/productSizeColor")
public class ProductSizeColorController {
        private final ProductSizeColorService productSizeColorService;

        @PostMapping("/bestSeling")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiMetaRes<List<ProductSizeColorBestSelling>> getBestSellingProductSize(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "7") int size) {

                Page<ProductSizeColorBestSelling> productSizes = productSizeColorService.getBestSellingProductSize(page,
                                size);
                MetadataDTO metadata = new MetadataDTO(
                                productSizes.getTotalElements(),
                                productSizes.getTotalPages(),
                                productSizes.getNumber(),
                                productSizes.getSize());

                return ApiMetaRes.<List<ProductSizeColorBestSelling>>builder().metadata(metadata)
                                .result(productSizes.getContent()).code(1000)
                                .message("addproduct size success").build();
        }
}