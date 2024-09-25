package com.shopro.shop1905.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.entities.Color;
import com.shopro.shop1905.entities.ProductSize;
import com.shopro.shop1905.entities.ProductSizeColor;

@Repository
public interface ProductSizeColorRepository extends JpaRepository<ProductSizeColor, Long> {
    Optional<ProductSizeColor> findByProductSizeAndColor(ProductSize productSize,
            Color color);

    @Query("SELECT psc FROM ProductSizeColor psc JOIN FETCH psc.productSize ps JOIN FETCH ps.product WHERE psc.id = :pscId")
    Optional<ProductSizeColor> findByIdFetchProductSizeAndFetchProduct(long pscId);

    @Query("SELECT psc FROM ProductSizeColor psc JOIN FETCH psc.productSize WHERE psc.id = :pscId")
    Optional<ProductSizeColor> findByIdFetchProductSize(long pscId);

}