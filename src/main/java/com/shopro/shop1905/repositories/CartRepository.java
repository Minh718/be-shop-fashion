package com.shopro.shop1905.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
}