package com.shopro.shop1905.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.constants.RoleUser;
import com.shopro.shop1905.dtos.EmailDetailsDto;
import com.shopro.shop1905.dtos.dtosReq.GetTokenGoogleReq;
import com.shopro.shop1905.dtos.dtosReq.RefreshTokenDTO;
import com.shopro.shop1905.dtos.dtosReq.UserLogin;
import com.shopro.shop1905.dtos.dtosReq.UserRegisterEmail;
import com.shopro.shop1905.dtos.dtosRes.GetTokenGoogleRes;
import com.shopro.shop1905.dtos.dtosRes.OutBoundInfoUser;
import com.shopro.shop1905.dtos.dtosRes.Token;
import com.shopro.shop1905.dtos.dtosRes.UserInfoToken;
import com.shopro.shop1905.entities.Cart;
import com.shopro.shop1905.entities.ChatBox;
import com.shopro.shop1905.entities.Role;
import com.shopro.shop1905.entities.User;
import com.shopro.shop1905.enums.TypeLogin;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.helpers.HashValue;
import com.shopro.shop1905.mappers.UserMapper;
import com.shopro.shop1905.repositories.ChatBoxRepository;
import com.shopro.shop1905.repositories.RoleRepository;
import com.shopro.shop1905.repositories.UserRepository;
import com.shopro.shop1905.repositories.httpClient.OutboundIdentityClientGoogle;
import com.shopro.shop1905.repositories.httpClient.OutboundInfoUserGoogle;
import com.shopro.shop1905.templates.Template;
import com.shopro.shop1905.util.RamdomKeyUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    MailService mailService;
    RedisService redisService;
    UserRepository userRepository;
    JwtService jwtService;
    RoleRepository roleRepository;
    ChatBoxRepository chatBoxRepository;
    VoucherService voucherService;
    OutboundInfoUserGoogle outboundInfoUserGoogle;
    OutboundIdentityClientGoogle outboundIdentityClientGoogle;
    Template template;

    @NonFinal
    @Value("${security.jwt.expiration-time-access}")
    long expAccessToken;

    @NonFinal
    @Value("${security.jwt.expiration-time-refresh}")
    long expRefreshToken;

    @NonFinal
    @Value("${security.jwt.expiration-time-access-admin}")
    long expAccessTokenAdmin;

    @NonFinal
    @Value("${security.jwt.expiration-time-refresh-admin}")
    long expRefreshTokenAdmin;

    @NonFinal
    @Value("${auth.google.client-id}")
    String CLIENT_ID;

    @NonFinal
    @Value("${frontend.host}")
    String FRONT_END;

    @NonFinal
    @Value("${auth.google.client-secret}")
    String CLIENT_SECRET;

    @NonFinal
    @Value("${auth.google.redirect-uri}")
    String REDIRECT_URI;

    @NonFinal
    String GRANT_TYPE = "authorization_code";

    @NonFinal
    @Value("${security.jwt.refresh-key}")
    String refreshKey;
    @NonFinal
    @Value("${security.jwt.refresh-key-admin}")
    String refreshKeyAdmin;

    public void RegisterUserByEmail(UserRegisterEmail userRegisterEmail) throws IOException {
        Optional<User> user = userRepository.findByEmail(userRegisterEmail.getEmail());
        if (user.isPresent())
            throw new CustomException(ErrorCode.USER_EXISTED);

        String uuid = java.util.UUID.randomUUID().toString();

        redisService.setKeyinMinutes(uuid + ":email", userRegisterEmail.getEmail(), 5);
        redisService.setKeyinMinutes(uuid + ":password", userRegisterEmail.getPassword(), 5);
        EmailDetailsDto emailDetailsDto = new EmailDetailsDto();
        emailDetailsDto.setRecipient(userRegisterEmail.getEmail());
        emailDetailsDto.setEmailSubject("xác nhận đăng ký");
        emailDetailsDto
                .setEmailBody(template.getTemplateEmail(uuid, userRegisterEmail.getEmail()));
        mailService.sendSimpleMail(emailDetailsDto);
    }

    // public Boolean confirmEmail(String token) {
    // return redisService.checkKeyExist(token);
    // }

    public void completeRegister(String token) {
        Boolean isExist = redisService.checkKeyExist(token + ":email");
        if (isExist.booleanValue()) {
            String email = (String) redisService.getKey(token + ":email");
            String password = (String) redisService.getKey(token + ":password");
            redisService.delKey(token + ":email");
            redisService.delKey(token + ":password");
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            String passwordHash = passwordEncoder.encode(password);
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById(RoleUser.USER_ROLE).ifPresent(roles::add);
            User user = new User();
            Cart cart = new Cart();
            user.setCart(cart);
            user.setEmail(email);
            user.setPassword(passwordHash);
            user.setRoles(roles);
            user.setCart(cart);
            user.setTypeLogin(TypeLogin.EMAIL);
            user.setKeyToken(RamdomKeyUtil.generateRandomKey());
            userRepository.save(user);
            addChatBoxForUser(user.getId());
            voucherService.addVouchersToNewUser(user);
        } else
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
    }

    private void addChatBoxForUser(String userId) {
        ChatBox chatBox = new ChatBox();
        // String adminId = userService.getIdAdmin();
        chatBox.setUserId(userId);
        // chatBox.setAdminId(adminId);
        chatBoxRepository.save(chatBox);
    }

    public UserInfoToken UserLoginByEmail(UserLogin userLogin) {
        User user = userRepository.findByEmailOrPhone(userLogin.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(userLogin.getPassword(), user.getPassword());
        if (!authenticated)
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        redisService.setKeyInMilliseconds("keyToken:" + user.getId(), user.getKeyToken(), expRefreshToken);
        redisService.addLoyalUser(user.getId());
        return AttachInfoUserWithToken(user);
    }

    private UserInfoToken AttachInfoUserWithToken(User user) {
        UserInfoToken userInfo = UserMapper.INSTANCE.toUserInfoToken(user);
        userInfo.setAccessToken(jwtService.generateToken(user, user.getKeyToken(), expAccessToken));
        userInfo.setRefreshToken(jwtService.generateToken(user, refreshKey, expRefreshToken));
        return userInfo;
    }

    public UserInfoToken UserLoginByGoogle(String code) {
        GetTokenGoogleReq getTokenGoogleReq = GetTokenGoogleReq.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(FRONT_END + REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build();
        GetTokenGoogleRes getTokenGoogleRes = outboundIdentityClientGoogle.getToken(
                getTokenGoogleReq);
        OutBoundInfoUser infoUser = outboundInfoUserGoogle.getInfoUser("json", getTokenGoogleRes.getAccessToken());
        User user = userRepository.findByidUserGoogle(infoUser.id()).orElseGet(() -> {
            User newUser = User.builder()
                    .idUserGoogle(infoUser.id())
                    .name(infoUser.name())
                    .picture(infoUser.picture())
                    .typeLogin(TypeLogin.OAUTH2)
                    .isactive(true)
                    .build();
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById(RoleUser.USER_ROLE).ifPresent(roles::add);
            newUser.setRoles(roles);
            newUser.setKeyToken(RamdomKeyUtil.generateRandomKey());
            Cart cart = new Cart();
            newUser.setCart(cart);
            userRepository.save(newUser);
            addChatBoxForUser(newUser.getId());

            voucherService.addVouchersToNewUser(newUser);
            return newUser;
        });
        redisService.setKeyInMilliseconds("keyToken:" + user.getId(), user.getKeyToken(), expRefreshToken);
        redisService.addLoyalUser(user.getId());
        return AttachInfoUserWithToken(user);
    }

    public Token refreshToken(RefreshTokenDTO refreshTokenDTO, String refreshKey, boolean isAdmin) {
        validateRefreshToken(refreshTokenDTO, refreshKey);
        User user = findUserByToken(refreshTokenDTO.refreshToken(), refreshKey, isAdmin);

        String hashToken = HashValue.hashString(refreshTokenDTO.refreshToken());
        if (isTokenUsed(user, hashToken)) {
            handleUsedToken(user);
            throw new CustomException(ErrorCode.INVALID_REFRESHTOKEN);
        }

        updateUserWithNewRefreshToken(user, hashToken);
        return generateTokens(user, isAdmin);
    }

    private User findUserByToken(String token, String refreshKey, boolean isAdmin) {
        String userId = jwtService.extractIdUser(token, refreshKey);
        if (isAdmin) {
            return userRepository.findByIdAndRole(userId, RoleUser.ADMIN_ROLE)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
        } else {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
        }
    }

    private Token generateTokens(User user, boolean isAdmin) {
        Token tokens = new Token();
        tokens.setAccessToken(
                jwtService.generateToken(user, user.getKeyToken(), isAdmin ? expAccessTokenAdmin : expAccessToken));
        tokens.setRefreshToken(jwtService.generateToken(user, isAdmin ? refreshKeyAdmin : refreshKey,
                isAdmin ? expRefreshTokenAdmin : expRefreshToken));
        return tokens;
    }

    private boolean isTokenUsed(User user, String hashToken) {
        return user.getRefreshTokenUsed().contains(hashToken);
    }

    private void validateRefreshToken(RefreshTokenDTO refreshTokenDTO, String refreshKey) {
        if (!jwtService.isTokenValid(refreshTokenDTO.refreshToken(), refreshKey)) {
            throw new CustomException(ErrorCode.INVALID_REFRESHTOKEN);
        }
    }

    private void updateUserWithNewRefreshToken(User user, String hashToken) {
        Set<String> refreshTokens = user.getRefreshTokenUsed();
        refreshTokens.add(hashToken);
        user.setRefreshTokenUsed(refreshTokens);
        userRepository.save(user);
    }

    private void handleUsedToken(User user) {
        String newKey = RamdomKeyUtil.generateRandomKey();
        user.setKeyToken(newKey);
        user.setRefreshTokenUsed(new HashSet<>());
        redisService.setKey("keyToken:" + user.getId(), newKey);
        userRepository.save(user);
    }

    // Public methods to call for each role
    public Token refreshTokenUser(RefreshTokenDTO refreshTokenDTO) {
        return refreshToken(refreshTokenDTO, refreshKey, false);
    }

    public Token refreshTokenAdmin(RefreshTokenDTO refreshTokenDTO) {
        return refreshToken(refreshTokenDTO, refreshKeyAdmin, true);
    }

}
