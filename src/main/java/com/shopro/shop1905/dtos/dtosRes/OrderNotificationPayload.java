package com.shopro.shop1905.dtos.dtosRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotificationPayload {
    private String avatar;
    private String message;
}
