package com.shopro.shop1905.dtos.dtosRes.projections;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoChatBox {
    private Long idChatBox;
    private String idAdmin;
    private boolean isSeen;
}
