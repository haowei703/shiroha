package com.shiroha.moyugongming.service;

import org.springframework.web.socket.WebSocketMessage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface WebSocketClientService {
    /* 建立websocket连接 */
    CompletableFuture<Void> connectAsync() throws InterruptedException;

    /* 关闭websocket连接 */
    void disconnect();

    /* 发送消息 */
    CompletableFuture<WebSocketMessage<?>> sendMessage(WebSocketMessage<?> message) throws IOException;
}
