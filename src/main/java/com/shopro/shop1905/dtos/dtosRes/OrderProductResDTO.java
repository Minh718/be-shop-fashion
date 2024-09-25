package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class OrderProductResDTO {
    private Long id;
    private long quantity;
    private long price;
    private String image;
    private String name;
    private String size;
    private String color;
}
