package com.shopro.shop1905.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosReq.VoucherDTO;
import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.entities.UserVoucher;
import com.shopro.shop1905.entities.UserVoucherId;
import com.shopro.shop1905.entities.Voucher;
import com.shopro.shop1905.enums.TypeAddVoucher;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.mappers.VoucherMapper;
import com.shopro.shop1905.repositories.UserRepository;
import com.shopro.shop1905.repositories.UserVoucherRepository;
import com.shopro.shop1905.repositories.VoucherRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final UserVoucherRepository userVoucherRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    @Autowired
    @Lazy
    private VoucherService self;

    public List<Voucher> getAllVouchers() {
        // List<Voucher> vouchers = null;
        String idUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Voucher> vouchers = userVoucherRepository.findAllByIdUserAndStillApply(idUser);
        return vouchers;
    }

    public Page<Voucher> getVouchers(int page, int size) {
        String idUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page, size);
        Page<Voucher> vouchers = userVoucherRepository.findAllByIdUserAndStillApply(idUser, pageable);
        return vouchers;
    }

    @Transactional
    public Voucher createVoucher(VoucherDTO voucherDTO, TypeAddVoucher type) {
        Voucher voucher = VoucherMapper.INSTANCE.toVoucher(voucherDTO);
        voucherRepository.save(voucher);

        if (type == TypeAddVoucher.GLOBAL) {
            // Retrieve all users
            List<User> allUsers = userRepository.findAll();

            // Create a UserVoucher for each user with the saved voucher
            self.addVoucherToUsers(voucher, allUsers);
            // userVoucherService.createUserVouchersForAllUsers(allUsers, voucher);
        } else if (type == TypeAddVoucher.NEW_USER) {
            voucher.setForNewUser(true);
        } else {
            List<Object> idUserLoyersObjects = redisService.getStringArray("loyalUser");
            List<String> idUserLoyers = idUserLoyersObjects.stream()
                    .map(object -> (String) object)
                    .collect(Collectors.toList());
            List<User> users = userRepository.findAllById(idUserLoyers);
            System.err.println(users.size());
            System.err.println(idUserLoyers.toString());
            self.addVoucherToUsers(voucher, users);

        }

        return voucher;
    }

    @Async("ThreadPoolUserVoucher")
    public void addVouchersToNewUser(User user) {
        List<Voucher> vouchers = voucherRepository.findAllForNewUser();
        for (Voucher voucher : vouchers) {
            UserVoucherId userVoucherId = new UserVoucherId(user.getId(), voucher.getId());
            UserVoucher userVoucher = new UserVoucher();
            userVoucher.setId(userVoucherId);
            userVoucher.setUser(user);
            userVoucher.setVoucher(voucher);
            userVoucherRepository.save(userVoucher);
        }
    }

    @Async("ThreadPoolUserVoucher")
    public void addVoucherToUsers(Voucher voucher, List<User> users) {
        for (User user : users) {
            UserVoucherId userVoucherId = new UserVoucherId(user.getId(), voucher.getId());
            UserVoucher userVoucher = new UserVoucher();
            userVoucher.setId(userVoucherId);
            userVoucher.setUser(user);
            userVoucher.setVoucher(voucher);
            userVoucherRepository.save(userVoucher);
        }
    }

    public Voucher checkVoucher(String code) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new CustomException(ErrorCode.VOUCHER_NOT_FOUND));

        if (!voucher.isActive()) {
            throw new CustomException(ErrorCode.VOUCHER_NOT_ACTIVE);
        }

        LocalDate today = LocalDate.now();

        if (voucher.getStartDate().isAfter(today)) {
            throw new CustomException(ErrorCode.BAD_REQUEST,
                    "This voucher starts on " + voucher.getStartDate().toString());
        }

        if (voucher.getEndDate().isBefore(today)) {
            throw new CustomException(ErrorCode.BAD_REQUEST,
                    "This voucher ended on " + voucher.getEndDate().toString());
        }

        return voucher;
    }

    @Async
    public void createUserVouchersForAllUsers(List<User> users, Voucher voucher) {
        for (User user : users) {
            UserVoucher userVoucher = new UserVoucher();
            userVoucher.setUser(user);
            userVoucher.setVoucher(voucher);
            userVoucherRepository.save(userVoucher);
        }
    }

    public Voucher deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherRepository.delete(voucher);
        return voucher;
    }

    public Voucher updateVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }
}
