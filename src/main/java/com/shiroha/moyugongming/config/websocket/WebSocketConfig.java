package com.shiroha.moyugongming.config.websocket;

import com.shiroha.moyugongming.controller.VideoSocketController;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(new VideoSocketController(), "ws/video").setAllowedOrigins("*")
                .addInterceptors(new AuthChannelInterceptor());
    }
}
