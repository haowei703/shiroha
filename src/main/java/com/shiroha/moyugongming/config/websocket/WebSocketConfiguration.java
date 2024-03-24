package com.shiroha.moyugongming.config.websocket;

import com.shiroha.moyugongming.handler.VideoSocketHandler;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket服务端配置类
 */
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(videoSocketHandler(), "ws/video").setAllowedOrigins("*")
                .addInterceptors(new AuthChannelInterceptor());
    }

    @Bean
    public WebSocketHandler videoSocketHandler() {
        return new VideoSocketHandler();
    }
}
