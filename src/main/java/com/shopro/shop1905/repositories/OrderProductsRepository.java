package com.shopro.shop1905.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.dtos.dtosRes.ProductSellingQuantityDto;
import com.shopro.shop1905.entities.OrderProduct;

@Repository
public interface OrderProductsRepository extends JpaRepository<OrderProduct, Long> {
        @Query("SELECT new com.shopro.shop1905.dtos.dtosRes.ProductSellingQuantityDto(op.productSizeColor,SUM(op.quantity)) "
                        +
                        "FROM OrderProduct op " +
                        "WHERE op.order.createdAt BETWEEN :startDate AND :endDate AND op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED "
                        +
                        "GROUP BY op.productSizeColor.id " +
                        "ORDER BY SUM(op.quantity) DESC")

        Page<ProductSellingQuantityDto> getBestSellingProducts(LocalDateTime startDate, LocalDateTime endDate,
                        Pageable pageable);
}
