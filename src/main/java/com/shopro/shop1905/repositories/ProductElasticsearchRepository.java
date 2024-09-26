package com.shopro.shop1905.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.entities.ProductDocument;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    Page<ProductDocument> findByNameAndStatus(String name, boolean status, Pageable pageable);
}
