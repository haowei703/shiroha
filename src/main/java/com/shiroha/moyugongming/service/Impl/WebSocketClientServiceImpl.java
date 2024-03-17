package com.shiroha.moyugongming.service.Impl;

import com.shiroha.moyugongming.handler.WebSocketClientHandler;
import com.shiroha.moyugongming.manager.CustomWebSocketConnectionManager;
import com.shiroha.moyugongming.service.WebSocketClientService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class WebSocketClientServiceImpl implements WebSocketClientService {

    private final WebSocketClientHandler handler;

    private final CustomWebSocketConnectionManager manager;

    @Getter
    private boolean connected = false;

    @Autowired
    public WebSocketClientServiceImpl(@Qualifier("CustomWebSocketConnectionManager") CustomWebSocketConnectionManager manager) {
        this.manager = manager;
        this.handler = manager.getHandler();
    }

    @Override
    public CompletableFuture<Void> connectAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        manager.start();
        this.connected = true;
        handler.setConnectedHandler(future::complete);
        return future;
    }

    @Override
    public void disconnect() {
        manager.stop();
        this.connected = false;
    }

    /**
     * 异步发送消息，并在回调中获取回传的消息
     *
     * @param message 发送给服务端的消息
     * @return future
     * @throws IOException
     */
    @Override
    public CompletableFuture<WebSocketMessage<?>> sendMessage(WebSocketMessage<?> message) throws IOException {
        WebSocketSession session = handler.getSession();
        session.sendMessage(message);
        CompletableFuture<WebSocketMessage<?>> future = new CompletableFuture<>();
        handler.setResponseHandler(future::complete);
        return future;
    }
}
