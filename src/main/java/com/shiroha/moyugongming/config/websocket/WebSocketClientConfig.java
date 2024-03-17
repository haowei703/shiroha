package com.shiroha.moyugongming.config.websocket;

import com.shiroha.moyugongming.handler.WebSocketClientHandler;
import com.shiroha.moyugongming.manager.CustomWebSocketConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocket客户端配置类
 */
@Configuration
public class WebSocketClientConfig {
    @Value("${python.server.url}")
    private String webSocketUri;

    @Bean
    public CustomWebSocketConnectionManager CustomWebSocketConnectionManager() throws URISyntaxException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketClientHandler handler = new WebSocketClientHandler();
        URI serverUri = new URI(webSocketUri);
        CustomWebSocketConnectionManager manager = new CustomWebSocketConnectionManager(webSocketClient, handler, serverUri);
        manager.setAutoStartup(false);
        return manager;
    }
}
