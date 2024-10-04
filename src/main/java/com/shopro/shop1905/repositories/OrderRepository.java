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
                        " (SELECT CURRENT_DATE - INTERVAL '1 day' * n AS date\n" +
                        " FROM generate_series(0, 6) AS n) AS days\n"
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
                        " (SELECT TO_CHAR(DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month' * n, 'YYYY-MM') AS month\n"
                        +
                        " FROM generate_series(0, 6) AS n) AS months\n" +
                        "LEFT JOIN \n" +
                        " tbl_order o ON TO_CHAR(o.created_at, 'YYYY-MM') = months.month \n" +
                        " AND o.created_at >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '6 months' \n" +
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
                        " (SELECT EXTRACT(WEEK FROM CURRENT_DATE - INTERVAL '1 week' * n) AS week\n" +
                        " FROM generate_series(0, 6) AS n) AS weeks\n"
                        +
                        "LEFT JOIN \n" +
                        " tbl_order o ON weeks.week = EXTRACT(WEEK FROM o.created_at) \n" +
                        " AND o.created_at >= CURRENT_DATE - INTERVAL '7 weeks' \n" +
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
                        " (SELECT EXTRACT(YEAR FROM CURRENT_DATE) - n AS year\n" +
                        " FROM generate_series(0, 6) AS n) AS years\n" +
                        "LEFT JOIN \n" +
                        " tbl_order o ON EXTRACT(YEAR FROM o.created_at) = years.year \n" +
                        " AND o.created_at >= CURRENT_DATE - INTERVAL '7 years' \n" +
                        " AND o.order_status != 'CANCELED'\n" +
                        "GROUP BY \n" +
                        " years.year\n" +
                        "ORDER BY \n" +
                        " years.year;")

        List<Object[]> statisticRevennue7LastYears();

}