package com.shopro.shop1905.dtos.dtosRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticRevennueDTO {

    private String time;
    private Long revenue;

    // Getters and Setters
}
