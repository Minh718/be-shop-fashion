package com.shopro.shop1905.dtos.dtosRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticOrderDTO {
    private long totalOrders;
    private double totalRevenue;
    private long totalQuantity;

}
