package com.shopro.shop1905.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.constants.RoleUser;
import com.shopro.shop1905.dtos.dtosRes.UserInfoToken;
import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.mappers.UserMapper;
import com.shopro.shop1905.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisService redisService;

    @NonFinal
    @Value("${security.jwt.expiration-time-access}")
    long expAccessToken;

    @NonFinal
    @Value("${security.jwt.expiration-time-refresh}")
    long expRefreshToken;

    @NonFinal
    @Value("${security.jwt.refresh-key}")
    String refreshKey;

    public UserInfoToken getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
        redisService.addLoyalUser(user.getId());
        UserInfoToken userInfo = UserMapper.INSTANCE.toUserInfoToken(user);
        return userInfo;
    }

    public String getIdAdmin() {
        Object id = redisService.getKey("idAdmin");
        if (id == null) {
            User user = userRepository.findByRolesName(RoleUser.ADMIN_ROLE)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
            redisService.setKeyInDate(refreshKey, user.getId(), 1);
            return user.getId();
        }
        return id.toString();
    }
}