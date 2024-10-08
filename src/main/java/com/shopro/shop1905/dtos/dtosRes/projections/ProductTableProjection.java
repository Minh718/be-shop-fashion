package com.shopro.shop1905.dtos.dtosRes.projections;

public interface ProductTableProjection {
    Long getId();

    String getName();

    Long getPrice();

    String getImage();

    boolean getStatus();

    int getTotalSales();

    Long getTotalRevenue();
}
