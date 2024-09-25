package com.shopro.shop1905.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosRes.ProductSizeAdmin;
import com.shopro.shop1905.repositories.SizeRepository;

import lombok.RequiredArgsConstructor;

/**
 * SizeService
 */
@Service
@RequiredArgsConstructor
public class SizeService {

    private final SizeRepository sizeRepository;
    private final RedisService redisService;

    public List<ProductSizeAdmin> getAllSizeOfProductForAdmin(Long id) {
        List<ProductSizeAdmin> sizes = sizeRepository.findAllSizesOfProduct(id);
        sizes.forEach(size -> {
            System.err.println(size.toString());
            var totalSales = redisService.getKey("productSize:" + size.getId());
            if (totalSales != null) {
                size.setTotalQuantity((int) totalSales);
            }
        });
        return sizes;
    }
    // // @PreAuthorize("hasRole('ADMIN')")
    // public void deleteSizesByIdCategory(Long categoryId) {
    // sizeRepository.deleteAllByCategoryId(categoryId);
    // }

}