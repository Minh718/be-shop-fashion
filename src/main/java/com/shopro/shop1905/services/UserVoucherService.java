package com.shopro.shop1905.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.entities.UserVoucher;
import com.shopro.shop1905.entities.Voucher;
import com.shopro.shop1905.repositories.UserVoucherRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserVoucherService {

    private final UserVoucherRepository userVoucherRepository;

    @Async("ThreadPoolVoucher")
    public void createUserVouchersForAllUsers(List<User> users, Voucher voucher) {
        for (User user : users) {
            UserVoucher userVoucher = new UserVoucher();
            userVoucher.setUser(user);
            userVoucher.setVoucher(voucher);
            userVoucherRepository.save(userVoucher);
        }
    }
}