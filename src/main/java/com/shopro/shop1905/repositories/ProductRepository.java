package com.shopro.shop1905.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.dtos.dtosRes.projections.ProductTableProjection;
import com.shopro.shop1905.entities.Product;
import com.shopro.shop1905.entities.SubCategory;

import feign.Param;
import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
        @Query("SELECT  p FROM Product p LEFT JOIN FETCH p.detailProduct  LEFT JOIN FETCH p.productSizes ps LEFT JOIN FETCH ps.productSizeColors  WHERE p.id = :id")
        Optional<Product> findByIdAndFetchProductSizesAndFetchDetailProduct(Long id);

        // Optional<Product> findById
        // Page<Product> findAllByOrderByCreatedDateDesc(Pageable pageable);
        @Query("SELECT  p FROM Product p LEFT JOIN FETCH p.detailProduct  LEFT JOIN FETCH p.subCategory LEFT JOIN FETCH p.brand  WHERE p.id = :id")
        Optional<Product> findByIdAndFetchDetailProduct(Long id);

        Page<Product> findAllByIsPublishAndSubCategory(boolean isPublish, SubCategory subCategory, Pageable pageable);

        // Page<Product> findAllByIsPublishOrderByCreatedDateDesc(boolean isPublish,
        // Pageable pageable);

        // @Query("SELECT p.id as id, p.name as name, p.price as price, p.image as
        // image, p.createdDate as createdDate, p.isPublish as status, SUM(op.quantity)
        // as totalSales, SUM(op.quantity) * p.price as totalRevenue "
        // +
        // "FROM OrderProduct op " +
        // "JOIN op.productSizeColor psc " +
        // "JOIN psc.productSize ps " +
        // "JOIN ps.product p " +
        // "WHERE op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED
        // " +
        // "GROUP BY p.id, p.name, p.price, p.image, p.isPublish ")
        // Page<ProductTableProjection> findAllProductForTable(Pageable pageable);
        @Query("SELECT p.id as id, p.name as name, p.price as price, p.image as image, p.createdDate as createdDate, p.isPublish as status, "
                        +
                        "COALESCE(SUM(op.quantity), 0) as totalSales, " +
                        "COALESCE(SUM(op.quantity) * p.price, 0) as totalRevenue " +
                        "FROM Product p " +
                        "LEFT JOIN p.productSizes ps " +
                        "LEFT JOIN ps.productSizeColors psc " +
                        "LEFT JOIN psc.orderProducts op " +
                        "ON op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED " +
                        "GROUP BY p.id, p.name, p.price, p.image, p.createdDate, p.isPublish")
        Page<ProductTableProjection> findAllProductForTable(Pageable pageable);

        @Query("SELECT p.id as id, p.name as name, p.price as price, p.image as image, p.createdDate as createdDate, p.isPublish as status, "
                        +
                        "COALESCE(SUM(op.quantity), 0) as totalSales, " +
                        "COALESCE(SUM(op.quantity) * p.price, 0) as totalRevenue " +
                        "FROM Product p " +
                        "LEFT JOIN p.productSizes ps " +
                        "LEFT JOIN ps.productSizeColors psc " +
                        "LEFT JOIN psc.orderProducts op ON op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED "
                        +
                        "WHERE p.categoryId = :idCategory " +
                        "GROUP BY p.id, p.name, p.price, p.image, p.createdDate, p.isPublish")
        Page<ProductTableProjection> findAllProductForTableByCategory(Long idCategory, Pageable pageable);

        @Query("SELECT p.id as id, p.name as name, p.price as price, p.image as image, p.createdDate as createdDate, p.isPublish as status, "
                        +
                        "COALESCE(SUM(op.quantity), 0) as totalSales, " +
                        "COALESCE(SUM(op.quantity) * p.price, 0) as totalRevenue " +
                        "FROM Product p " +
                        "LEFT JOIN p.productSizes ps " +
                        "LEFT JOIN ps.productSizeColors psc " +
                        "LEFT JOIN psc.orderProducts op ON op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED "
                        +
                        "WHERE p.subCategory.id = :idSubCategory " +
                        "GROUP BY p.id, p.name, p.price, p.image, p.createdDate, p.isPublish")
        Page<ProductTableProjection> findAllProductForTableBySubCategory(Long idSubCategory, Pageable pageable);

        Page<Product> findAllByIsPublish(boolean isPublish, Pageable pageable);

        Page<Product> findAllByIsPublishAndCategoryIdOrderByCreatedDateDesc(boolean isPublish, Long categoryId,
                        Pageable pageable);

        // Page<Product> findAllByIsDraftOrderByCreatedDateDesc(boolean isDraft,
        // Pageable pageable);

        @Transactional
        @Modifying
        @Query("UPDATE Product p SET p.isPublish = true, p.isDraft = false WHERE p.id = :id")
        int updatePublishProductById(@Param("id") Long id);

        // List<Product> findAllByOrderByNameDesc();
}