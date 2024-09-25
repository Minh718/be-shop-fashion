package com.shopro.shop1905.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosReq.VoucherDTO;
import com.shopro.shop1905.entities.Voucher;

@Mapper
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);

    Voucher toVoucher(VoucherDTO voucherDTO);
}
