package com.shopro.shop1905.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosReq.VoucherDTO;
import com.shopro.shop1905.entities.Voucher;

@Mapper
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "forNewUser", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Voucher toVoucher(VoucherDTO voucherDTO);
}
