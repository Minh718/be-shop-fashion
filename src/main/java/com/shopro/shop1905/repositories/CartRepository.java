package com.shopro.shop1905.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.entities.Cart;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    @Modifying
    @Transactional
    @Query("update Cart c set c.quantity = c.quantity - 1 where c.id = :id")
    void updateQuantityById(@Param("id") UUID id);
}