package com.shopro.shop1905.dtos.dtosRes;

import java.util.UUID;

import lombok.Data;

@Data
public class CartDTO {
    private UUID id;
    private int quantity;
}
