package com.shopro.shop1905.dtos.dtosReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserSendMess {
    @NotNull(message = "Message is required")
    private String message;
}
