package com.shopro.shop1905.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosRes.ProductSellingQuantityDto;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorBestSelling;
import com.shopro.shop1905.entities.ProductSizeColor;
import com.shopro.shop1905.mappers.ProductSizeColorMapper;
import com.shopro.shop1905.repositories.OrderProductsRepository;
import com.shopro.shop1905.util.DateTimeUtil;

import lombok.RequiredArgsConstructor;

/**
 * ProductSizeService
 */
@RestController
@RequiredArgsConstructor
public class ProductSizeColorService {
    private final RedisService redisService;
    private final OrderProductsRepository orderProductsRepository;

    public Page<ProductSizeColorBestSelling> getBestSellingProductSize(int page, int size) {
        LocalDateTime start = DateTimeUtil.getCurrentVietnamTime().minusDays(1);
        LocalDateTime end = DateTimeUtil.getCurrentVietnamTime();
        Pageable pageable = PageRequest.of(page, size);

        Page<ProductSellingQuantityDto> productSellingQuantityDtos = orderProductsRepository
                .getBestSellingProducts(start, end, pageable);

        List<ProductSizeColorBestSelling> productSizeBestSellings = productSellingQuantityDtos.stream()
                .map(productSellingQuantityDto -> {
                    ProductSizeColor productSizeColor = productSellingQuantityDto.getProductSizeColor();
                    ProductSizeColorBestSelling productSizeBestSelling = ProductSizeColorMapper.INSTANCE
                            .toProductSizeColorBestSelling(productSizeColor);
                    productSizeBestSelling.setSold(productSellingQuantityDto.getQuantity());

                    var remain = redisService.getKey("productSizeColor:" + productSizeColor.getId());
                    if (remain == null) {
                        remain = 0;
                    }
                    productSizeBestSelling.setRemain((int) remain);

                    return productSizeBestSelling;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(productSizeBestSellings, pageable,
                productSellingQuantityDtos.getTotalElements());
    }
}