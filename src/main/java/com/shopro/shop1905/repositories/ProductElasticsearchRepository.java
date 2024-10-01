package com.shopro.shop1905.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.entities.ProductDocument;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    Page<ProductDocument> findByNameAndStatus(String name, boolean status, Pageable pageable);

    Page<ProductDocument> findByStatus(boolean status, Pageable pageable);

    Page<ProductDocument> findByStatusAndSubCategoryId(boolean status, long subCategoryId, Pageable pageable);

    Page<ProductDocument> findByStatusAndThump(boolean status, String thump, Pageable pageable);

    Page<ProductDocument> findByStatusAndCategoryId(boolean status, long categoryId, Pageable pageable);

}
