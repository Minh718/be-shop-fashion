package com.shopro.shop1905.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.dtos.dtosRes.ProductSizeAdmin;

import com.shopro.shop1905.dtos.dtosRes.projections.ProductTableProjection;
import com.shopro.shop1905.entities.Category;
import com.shopro.shop1905.entities.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    // @Query("delete from Size s where s.category.id = ?1")
    // @Modifying
    // @Transactional
    void deleteAllByCategoryId(Long categoryId);

    Optional<Size> findByIdAndCategoryId(Long id, Long categoryId);

    Set<Size> findAllByCategoryId(Long categoryId);

    Optional<Size> findByIdAndCategory(Long id, Category category);

    // @Query("SELECT new com.shopro.shop1905.dtos.dtosRes.ProductSizeAdmin(ps.id as
    // id, s.name as name, COALESCE(SUM(op.quantity), 0) as totalSales) " +
    // "FROM Size s " +
    // "LEFT JOIN s.productSizes ps " +
    // "LEFT JOIN ps.productSizeColors psc " +
    // "LEFT JOIN psc.orderProducts op " +
    // "ON op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED "
    // +
    // "WHERE ps.id = :id " +
    // "GROUP BY ps.id ")
    // List<ProductSizeAdmin> findAllSizeOfProductSize(Long id);
    @Query("SELECT new com.shopro.shop1905.dtos.dtosRes.ProductSizeAdmin(ps.id, s.name, COALESCE(SUM(op.quantity), 0) as totalSales) "
            +
            "FROM Size s " +
            "LEFT JOIN s.productSizes ps " +
            "LEFT JOIN ps.productSizeColors psc " +
            "LEFT JOIN psc.orderProducts op " +
            "ON op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED " +
            "WHERE ps.product.id = :id " +
            "GROUP BY ps.id, s.name ORDER BY ps.id asc")
    List<ProductSizeAdmin> findAllSizesOfProduct(Long id);

}