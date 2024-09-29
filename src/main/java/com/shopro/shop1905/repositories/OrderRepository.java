package com.shopro.shop1905.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.dtos.dtosRes.StatisticOrderDTO;
import com.shopro.shop1905.entities.TblOrder;
import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.enums.OrderStatus;

import feign.Param;

@Repository
public interface OrderRepository extends JpaRepository<TblOrder, Long> {
        // List<TblOrder> findAllByOrderStatus(OrderStatus orderStatus);

        @Query("SELECT o FROM TblOrder o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
        Page<TblOrder> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

        Page<TblOrder> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

        // List<TblOrder> findAllByOrderStatusAndUser(OrderStatus orderStatus, User
        // user);

        @Query("SELECT o FROM TblOrder o JOIN FETCH o.orderProducts WHERE o.orderStatus = :status AND o.user.id = :userId")
        List<TblOrder> findAllByOrderStatusAndUserIdFetchOrderProduct(@Param("status") OrderStatus status,
                        @Param("userId") String userId);

        @Query("SELECT o FROM TblOrder o JOIN FETCH o.orderProducts WHERE o.orderStatus = :status")
        List<TblOrder> findAllByOrderStatusFetchOrderProduct(@Param("status") OrderStatus status);

        @Query("SELECT o FROM TblOrder o JOIN FETCH o.orderProducts op JOIN FETCH op.productSizeColor psc JOIN FETCH psc.productSize WHERE o.id = :id ")
        Optional<TblOrder> findByIdFetchOrderProductFetchProductSizeColorFetchProductSize(Long id);
        // Page<TblOrder> findAllByUser(User user, Pageable pageable);

        // @Query("SELECT new
        // com.shopro.shop1905.dtos.dtosRes.StatisticOrderDTO(COUNT(o),
        // SUM(o.totalAmount), SUM(o.totalQuantity)) "
        // +
        // "FROM TblOrder o " +

        // "WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.orderStatus !=
        // com.shopro.shop1905.enums.OrderStatus.CANCELED")
        @Query("SELECT new com.shopro.shop1905.dtos.dtosRes.StatisticOrderDTO(" +
                        "COALESCE(COUNT(o), 0), " +
                        "COALESCE(SUM(o.totalAmount), 0), " +
                        "COALESCE(SUM(op.quantity), 0)) " +
                        "FROM TblOrder o JOIN o.orderProducts op " +
                        "WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.orderStatus != com.shopro.shop1905.enums.OrderStatus.CANCELED")
        StatisticOrderDTO findStatisticOrderByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

        @Query(nativeQuery = true, value = "SELECT \n" +
                        " days.date AS date,\n" +
                        " COALESCE(SUM(o.total_amount), 0) AS revenue\n" +
                        "FROM \n" +
                        " (SELECT CURDATE() - INTERVAL n DAY AS date\n" +
                        " FROM (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS nums) AS days\n"
                        +
                        "LEFT JOIN \n" +
                        " tbl_order o ON DATE(o.created_at) = days.date \n" +
                        " AND o.order_status != 'CANCELED'\n" +
                        "GROUP BY \n" +
                        " days.date\n" +
                        "ORDER BY \n" +
                        " days.date;")
        List<Object[]> statisticRevennue7LastDays();

        @Query(nativeQuery = true, value = "SELECT \n" +
                        " months.month AS month,\n" +
                        " COALESCE(SUM(o.total_amount), 0) AS revenue\n" +
                        "FROM \n" +
                        " (SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL n MONTH), '%Y-%m') AS month\n" +
                        " FROM (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS nums) AS months\n"
                        +
                        "LEFT JOIN \n" +
                        " tbl_order o ON DATE_FORMAT(o.created_at, '%Y-%m') = months.month \n" +
                        " AND o.created_at >= DATE_FORMAT(CURDATE() - INTERVAL 6 MONTH, '%Y-%m-01') \n" +
                        " AND o.order_status != 'CANCELED'\n" +
                        "GROUP BY \n" +
                        " months.month\n" +
                        "ORDER BY \n" +
                        " months.month;")
        List<Object[]> statisticRevennue7LastMonths();

        @Query(nativeQuery = true, value = "SELECT \n" +
                        " weeks.week AS week,\n" +
                        " COALESCE(SUM(o.total_amount), 0) AS revenue\n" +
                        "FROM \n" +
                        " (SELECT WEEK(DATE_SUB(CURDATE(), INTERVAL n WEEK), 1) AS week\n" +
                        " FROM (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS nums) AS weeks\n"
                        +
                        "LEFT JOIN \n" +
                        " tbl_order o ON weeks.week = WEEK(o.created_at, 1) \n" +
                        " AND o.created_at >= CURDATE() - INTERVAL 7 WEEK \n" +
                        " AND o.order_status != 'CANCELED'\n" +
                        "GROUP BY \n" +
                        " weeks.week\n" +
                        "ORDER BY \n" +
                        " weeks.week;")
        List<Object[]> statisticRevennue7LastWeeks();

        @Query(nativeQuery = true, value = "SELECT \n" +
                        " years.year AS year,\n" +
                        " COALESCE(SUM(o.total_amount), 0) AS revenue\n" +
                        "FROM \n" +
                        " (SELECT YEAR(CURDATE()) - n AS year\n" +
                        " FROM (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS nums) AS years\n"
                        +
                        "LEFT JOIN \n" +
                        " tbl_order o ON YEAR(o.created_at) = years.year \n" +
                        " AND o.created_at >= CURDATE() - INTERVAL 7 YEAR \n" +
                        " AND o.order_status != 'CANCELED'\n" +
                        "GROUP BY \n" +
                        " years.year\n" +
                        "ORDER BY \n" +
                        " years.year;")
        List<Object[]> statisticRevennue7LastYears();

}