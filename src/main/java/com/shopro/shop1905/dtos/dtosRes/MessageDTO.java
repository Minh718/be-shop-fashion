package com.shopro.shop1905.dtos.dtosRes;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    private Long idChatBox;
    private String idSend;
    private String message;
    private String createdAt;
}
