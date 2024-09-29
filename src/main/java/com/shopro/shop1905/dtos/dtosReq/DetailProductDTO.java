package com.shopro.shop1905.dtos.dtosReq;

import java.util.List;

import lombok.Data;

@Data
public class DetailProductDTO {
    private String description;
    private String material;
    private String origin;
    private String warranty;
    private String madeIn;
    private String model;
    private List<String> images;
}
