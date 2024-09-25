package com.shopro.shop1905.dtos.dtosRes;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiRes
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiMetaRes<T> {

    private int code;
    private String message;
    private T result;
    private MetadataDTO metadata;
}