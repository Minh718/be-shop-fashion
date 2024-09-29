package com.shopro.shop1905.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserVoucherId implements Serializable {
    private String userId;
    private Long voucherId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }
}
