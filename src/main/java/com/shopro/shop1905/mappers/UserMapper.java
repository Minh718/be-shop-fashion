package com.shopro.shop1905.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.UserInfo;
import com.shopro.shop1905.dtos.dtosRes.UserInfoToken;
import com.shopro.shop1905.entities.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(ignore = true, target = "accessToken")
    @Mapping(ignore = true, target = "refreshToken")
    UserInfoToken toUserInfoToken(User user);

    UserInfo toUserInfo(User user);
}