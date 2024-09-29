package com.shopro.shop1905.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.dtos.dtosRes.ProductColorsAdmin;
import com.shopro.shop1905.entities.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

    @Query(nativeQuery = true, value = "Select * from color where id NOT IN (SELECT psc.color_id FROM product_size_color psc where product_size_id = :idProductSize)")
    List<Color> findAllColorNotInProductSize(Long idProductSize);

    @Query("SELECT new com.shopro.shop1905.dtos.dtosRes.ProductColorsAdmin(psc.id, c.name, c.color, COALESCE(SUM(op.quantity), 0) as totalSales) "
            +
            "FROM Color c " +
            "LEFT JOIN c.productSizeColors psc " +
            "LEFT JOIN psc.orderProducts op " +
            "ON op.order.orderStatus IS NULL OR op.order.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED "
            +
            "WHERE psc.productSize.id = :id " +
            "GROUP BY psc.id, c.id, c.name, c.color")
    List<ProductColorsAdmin> findAllColorsOfProduct(Long id);

}
