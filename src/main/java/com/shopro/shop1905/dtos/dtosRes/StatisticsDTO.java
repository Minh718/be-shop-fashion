package com.shopro.shop1905.dtos.dtosRes;

import java.util.List;

/**
 * StatisticsDTO
 */

public record StatisticsDTO(StatisticCompare totalOrders, StatisticCompare totalRevenue,
        StatisticCompare totalQuantity, StatisticCompare totalNewCustommers,
        List<StatisticRevennueDTO> dataChart) {
}