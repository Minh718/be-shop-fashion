package com.shopro.shop1905.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.OutBoundInfoUser;
import com.shopro.shop1905.dtos.dtosRes.UserInfo;
import com.shopro.shop1905.dtos.dtosRes.UserInfoToken;
import com.shopro.shop1905.entities.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserInfoToken toUserInfoToken(User user);

    UserInfo toUserInfo(User user);

    // @Mapping(target = "id", ignore = true)
    // User toUser(OutBoundInfoUser outBoundInfoUser);

    // UserResponse toUserResponse(User user);

    // @Mapping(target = "roles", ignore = true)
    // void updateUser(@MappingTarget User user, UserUpdateRequest request);
}