package com.shopro.shop1905.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.StatisticsDTO;
import com.shopro.shop1905.enums.TypeStatistic;
import com.shopro.shop1905.services.StatisticService;

import lombok.RequiredArgsConstructor;

/**
 * StatisticController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/dashboard")
    public ApiRes<StatisticsDTO> getStatisticsDashboard(@RequestParam(defaultValue = "DAY") TypeStatistic type) {
        return ApiRes.<StatisticsDTO>builder()
                .result(statisticService.getStatisticsDashboard(type))
                .build();
    }

}