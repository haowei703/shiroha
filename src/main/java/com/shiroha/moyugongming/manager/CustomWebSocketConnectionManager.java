package com.shiroha.moyugongming.manager;

import com.shiroha.moyugongming.handler.WebSocketClientHandler;
import lombok.Getter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.net.URI;

/**
 * 自定义WebSocketConnectionManager子类，新增返回处理器对象方法
 */
@Getter
public class CustomWebSocketConnectionManager extends WebSocketConnectionManager {
    private final WebSocketClientHandler handler;

    public CustomWebSocketConnectionManager(WebSocketClient client, WebSocketClientHandler webSocketHandler, URI uri) {
        super(client, webSocketHandler, uri);
        this.handler = webSocketHandler;
    }
}
