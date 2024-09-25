package com.shopro.shop1905.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@SuppressWarnings("null") MessageBrokerRegistry config) {
        config.enableSimpleBroker("/all", "/specific");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000") // Only allow your frontend origin
                .setAllowedOriginPatterns("http://localhost:3000") // For Spring 5.3+
                .withSockJS() // Enable SockJS fallback for older browsers
                .setSessionCookieNeeded(true); // Enable sending session cookies

        // Regular WebSocket connection (without SockJS) if you want to support both
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                .setAllowedOriginPatterns("http://localhost:3000") // For Spring 5.3+
                .setAllowedOriginPatterns("http://localhost:3000");
    }
}