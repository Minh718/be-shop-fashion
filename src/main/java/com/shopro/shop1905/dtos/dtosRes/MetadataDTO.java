package com.shopro.shop1905.dtos.dtosRes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDTO {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private int itemsPerPage;

}
