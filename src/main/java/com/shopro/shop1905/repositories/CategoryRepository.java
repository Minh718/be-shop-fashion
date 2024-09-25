package com.shopro.shop1905.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}