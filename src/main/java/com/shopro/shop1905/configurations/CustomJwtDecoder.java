package com.shopro.shop1905.configurations;

import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.repositories.UserRepository;
import com.shopro.shop1905.services.JwtService;
import com.shopro.shop1905.services.RedisService;
import com.shopro.shop1905.services.UserService;

import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        String idUser = jwtService.extractId(token);

        if (Objects.isNull(idUser))
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        String keyToken = null;
        if (redisService.checkKeyExist("keyToken:" + idUser)) {
            keyToken = (String) redisService.getKey("keyToken:" + idUser);
        } else {
            User user = userRepository.findById(idUser)
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
            keyToken = user.getKeyToken();
            redisService.setKey("keyToken:" + idUser, keyToken);
        }
        byte[] keyBytes = Decoders.BASE64.decode(keyToken);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HS256");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        return nimbusJwtDecoder.decode(token);
    }
}
