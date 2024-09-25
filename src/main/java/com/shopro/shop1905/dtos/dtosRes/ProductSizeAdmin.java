package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class ProductSizeAdmin {
    private Long id;
    String name;
    long totalQuantity = 0;
    long totalSales;

    public ProductSizeAdmin(Long id, String name, long totalSales) {
        this.id = id;
        this.name = name;
        this.totalSales = totalSales;
    }

}
