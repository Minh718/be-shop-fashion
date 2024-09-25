package com.shopro.shop1905.util;

import com.shopro.shop1905.entities.User;

public class GetReToken {
    public static void GetReToken(User user) {
        System.out.println(user.getUserVouchers().size());
    }
}
