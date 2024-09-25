package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class ProductColorsAdmin {
    private Long id;
    private String name;
    private String color;
    private long totalQuantity = 0;
    private long totalSales;

    public ProductColorsAdmin(Long id, String name,String color, long totalSales) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.totalSales = totalSales;
    }

}
