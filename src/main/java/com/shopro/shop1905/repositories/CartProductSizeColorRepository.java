package com.shopro.shop1905.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import com.shopro.shop1905.entities.CartProductSizeColor;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface CartProductSizeColorRepository extends JpaRepository<CartProductSizeColor, Long> {
        @Query("SELECT cpsc FROM CartProductSizeColor cpsc WHERE cpsc.cart.id=:cartId And cpsc.productSizeColorId=:productSizeColorId")

        Optional<CartProductSizeColor> findByCartIdAndProductSizeColorId(UUID cartId,
                        long productSizeColorId);

        // java.util.List<CartProductSizeColor> findAllByCartId(UUID cartId);

        @Query("SELECT cpsc FROM CartProductSizeColor cpsc WHERE cpsc.cart.id = :cartId ORDER BY cpsc.updateAt DESC")
        Page<CartProductSizeColor> findAllByCartIdOrderByUpdateAtDesc(UUID cartId,
                        Pageable pageable);
        // findAllByCartIdAndFetchProductSizeColorsAndProductSizeAndProductOrderByUpdateAtDesc(
        // UUID cartId, Pageable pageable);
        // @Query("DELETE FROM CartProductSizeColor cpsc WHERE cpsc.cart.id = :cartId
        // And cpsc.productSize.id = :productSizeId")
        // void deleteByCartIdAndProductSizeId(UUID cartId, long productSizeId);

        Optional<CartProductSizeColor> findByIdAndCartId(long id, UUID cartId);

        // @Query("SELECT cpsc FROM CartProductSizeColor cpsc JOIN FETCH
        // cpsc.productSizeColor psc JOIN FETCH psc.productSize ps LEFT JOIN FETCH
        // ps.product WHERE cpsc.id = :id")
        // Optional<CartProductSizeColor> findByIdWithProduct(@Param("id") long id);
}