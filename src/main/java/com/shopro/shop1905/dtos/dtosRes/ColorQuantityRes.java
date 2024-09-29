package com.shopro.shop1905.dtos.dtosRes;

import lombok.Data;

@Data
public class ColorQuantityRes {

    private Long id;
    private int quantity;

    private ColorRes color;
}
