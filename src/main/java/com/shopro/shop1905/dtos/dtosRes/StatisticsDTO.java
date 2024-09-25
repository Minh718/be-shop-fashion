package com.shopro.shop1905.dtos.dtosRes;

import java.util.List;
import java.util.Map;

import org.springframework.data.util.Pair;

/**
 * StatisticsDTO
 */

public record StatisticsDTO(StatisticCompare totalOrders, StatisticCompare totalRevenue,
                StatisticCompare totalQuantity, StatisticCompare totalNewCustommers,
                List<StatisticRevennueDTO> dataChart) {
}