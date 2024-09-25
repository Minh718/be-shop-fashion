package com.shopro.shop1905.dtos.dtosRes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * CartProduct
 */
@Getter
@Setter
@Builder
public class OrderProductDTO {

    private Long id;
    private long quantity;
    private ProductSizeDTO productSize;
}