package com.shopro.shop1905.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
        private final String[] PUBLIC_ENDPOINTS = {
                        "/ws/**", "/api/users", "/api/auth/*", "api/category/all", "/api/auth/register/*",
                        "/api/auth/login/*", "/api/product/search",
                        "/api/product/public/*",
                        "swagger-ui/**", "v3/api-docs/**", "swagger-ui.html", "swagger-ui/**",
                        "/api/payment/vn-pay-callback"

        };
        // @Autowired
        // private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
        @Autowired
        private CustomJwtDecoder customJwtDecoder;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable);
                // httpSecurity.cors(cors -> {
                // cors.configurationSource(request -> {
                // CorsConfiguration corsConfiguration = new CorsConfiguration();
                // corsConfiguration.applyPermitDefaultValues();
                // corsConfiguration.addAllowedMethod(HttpMethod.PUT);
                // corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
                // corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
                // return corsConfiguration;
                // });
                // });
                httpSecurity.authorizeHttpRequests(
                                request -> request.requestMatchers(PUBLIC_ENDPOINTS)
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/product/{id}")
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated());
                httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                                .bearerTokenResolver(this::tokenExtractor)
                                .jwt(jwtConfigurer -> jwtConfigurer
                                                .decoder(customJwtDecoder)
                                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
                // httpSecurity.exceptionHandling(exceptionHandling -> exceptionHandling
                // .authenticationEntryPoint(customAuthenticationEntryPoint));
                return httpSecurity.build();
        }

        @Bean
        JwtAuthenticationConverter jwtAuthenticationConverter() {
                JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

                return jwtAuthenticationConverter;
        }

        public String tokenExtractor(HttpServletRequest request) {
                String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (header != null)
                        return header.replace("Bearer ", "");
                String path = request.getServletPath();
                if (path.startsWith("/ws")) {
                        String token = request.getParameter("access_token");
                        if (token != null)
                                return token;
                }
                return null;
        }

        // @Bean
        // public CorsFilter corsFilter() {
        // UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // CorsConfiguration config = new CorsConfiguration();
        // config.addAllowedOrigin("*");
        // config.addAllowedHeader("*");
        // config.addAllowedMethod("*");
        // source.registerCorsConfiguration("/**", config);
        // return new CorsFilter(source);
        // }
}
