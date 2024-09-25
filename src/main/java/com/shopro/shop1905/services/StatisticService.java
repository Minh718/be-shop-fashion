package com.shopro.shop1905.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorBestSelling;
import com.shopro.shop1905.dtos.dtosRes.StatisticCompare;
import com.shopro.shop1905.dtos.dtosRes.StatisticOrderDTO;
import com.shopro.shop1905.dtos.dtosRes.StatisticRevennueDTO;
import com.shopro.shop1905.dtos.dtosRes.StatisticsDTO;
import com.shopro.shop1905.enums.TypeStatistic;
import com.shopro.shop1905.repositories.OrderRepository;
import com.shopro.shop1905.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductSizeColorService productSizeColorService;

    public StatisticsDTO getStatisticsDashboard(TypeStatistic type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start, prev, end;
        List<Object[]> results = null;

        // Determine time ranges and statistic method based on the type
        switch (type) {
            case DAY:
                start = now.minusDays(1);
                prev = now.minusDays(2);
                end = now;
                results = orderRepository.statisticRevennue7LastDays();
                break;
            case MONTH:
                start = now.minusMonths(1);
                prev = now.minusMonths(2);
                end = now;
                results = orderRepository.statisticRevennue7LastMonths();
                break;
            case WEEK:
                start = now.minusWeeks(1);
                prev = now.minusWeeks(2);
                end = now;
                results = orderRepository.statisticRevennue7LastWeeks();
                break;
            case YEAR:
                start = now.minusYears(1);
                prev = now.minusYears(2);
                end = now;
                results = orderRepository.statisticRevennue7LastYears();
                break;
            default:
                throw new RuntimeException("Type not found");
        }
        List<StatisticRevennueDTO> statisticRevennue = results.stream()
                .map(result -> new StatisticRevennueDTO(result[0].toString(), ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
        // Retrieve statistics
        StatisticOrderDTO prevStatistic = orderRepository.findStatisticOrderByCreatedAtBetween(prev, start);
        StatisticOrderDTO currentStatistic = orderRepository.findStatisticOrderByCreatedAtBetween(start, end);

        // Calculate percentage changes with zero-division check
        float percentTotalOrders = calculatePercentageChange(prevStatistic.getTotalOrders(),
                currentStatistic.getTotalOrders());
        float percentTotalRevenue = calculatePercentageChange(prevStatistic.getTotalRevenue(),
                currentStatistic.getTotalRevenue());
        float percentTotalQuantity = calculatePercentageChange(prevStatistic.getTotalQuantity(),
                currentStatistic.getTotalQuantity());

        // Retrieve new user counts
        long prevTotalUser = userRepository.countTotalNewUser(prev, start);
        long currentTotalUser = userRepository.countTotalNewUser(start, end);

        float percentNewCustomers = calculatePercentageChange(prevTotalUser,
                currentTotalUser);

        // Create and return the DTO
        StatisticCompare totalOrders = new StatisticCompare(currentStatistic.getTotalOrders(), percentTotalOrders);
        StatisticCompare totalRevenue = new StatisticCompare(currentStatistic.getTotalRevenue(), percentTotalRevenue);
        StatisticCompare totalQuantity = new StatisticCompare(currentStatistic.getTotalQuantity(),
                percentTotalQuantity);
        StatisticCompare totalNewCustomers = new StatisticCompare(currentTotalUser,
                percentNewCustomers);
        return new StatisticsDTO(totalOrders, totalRevenue, totalQuantity,
                totalNewCustomers,
                statisticRevennue);
    }

    // Helper method to avoid division by zero
    private float calculatePercentageChange(float previous, float current) {
        if (previous == 0) {
            return current == 0 ? 0 : 100; // If previous is 0 and current is not, return 100%
        }
        return (current - previous) / previous * 100;
    }

}
