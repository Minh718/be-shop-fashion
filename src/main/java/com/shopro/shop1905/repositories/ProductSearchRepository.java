package com.shopro.shop1905.repositories;

import java.util.List;

import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shopro.shop1905.entities.Product;

public interface ProductSearchRepository {
    Page<Product> searchPublicProduct(String text, Pageable pageable, List<String> fields, String sortBy,
            SortOrder sortOrder);
}
