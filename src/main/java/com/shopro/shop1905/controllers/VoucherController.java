package com.shopro.shop1905.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.VoucherDTO;
import com.shopro.shop1905.dtos.dtosRes.ApiMetaRes;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.MetadataDTO;
import com.shopro.shop1905.dtos.dtosRes.OrderResDTO;
import com.shopro.shop1905.entities.Voucher;
import com.shopro.shop1905.enums.TypeAddVoucher;
import com.shopro.shop1905.services.VoucherService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("/user/all")
    public ApiRes<List<Voucher>> getAllVouchers() {
        return ApiRes.<List<Voucher>>builder().result(voucherService.getAllVouchers()).message("get vouchers susccess")
                .build();
    }

    @GetMapping("/user/get")
    public ApiMetaRes<List<Voucher>> getVouchers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Voucher> vouchersPage = voucherService.getVouchers(page, size);
        MetadataDTO metadata = new MetadataDTO(
                vouchersPage.getTotalElements(),
                vouchersPage.getTotalPages(),
                vouchersPage.getNumber(),
                vouchersPage.getSize());
        return ApiMetaRes.<List<Voucher>>builder().code(1000).message("get vouchers success")
                .result(vouchersPage.getContent()).metadata(metadata)
                .build();
    }

    @PostAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ApiRes<Voucher> createVoucher(@RequestBody VoucherDTO voucherDTO,
            @RequestParam(defaultValue = "GLOBAL") TypeAddVoucher type) {
        return ApiRes.<Voucher>builder().result(voucherService.createVoucher(voucherDTO, type))
                .message("create voucher success")
                .build();
    }

    @PostAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public ApiRes<Voucher> createVoucher(@PathVariable Long id) {
        return ApiRes.<Voucher>builder().result(voucherService.deleteVoucher(id)).message("create voucher success")
                .build();
    }

    @PostAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public ApiRes<Voucher> updateVoucher(@PathVariable Long id) {
        return ApiRes.<Voucher>builder().result(voucherService.deleteVoucher(id)).message("create voucher success")
                .build();
    }

}
