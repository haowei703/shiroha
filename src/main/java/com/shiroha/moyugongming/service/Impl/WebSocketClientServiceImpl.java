package com.shiroha.moyugongming.service.Impl;

import com.shiroha.moyugongming.handler.WebSocketClientHandler;
import com.shiroha.moyugongming.service.WebSocketClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class WebSocketClientServiceImpl implements WebSocketClientService {
    private final URI serverUri;

    WebSocketClientHandler handler;
    private WebSocketConnectionManager manager;

    @Autowired
    public WebSocketClientServiceImpl(@Value("${python.server.host}") String serverHost, @Value("${python.server.port}") String serverPort) throws URISyntaxException {
        this.serverUri = new URI("ws://" + serverHost + ":" + serverPort);
        this.handler = new WebSocketClientHandler();
    }

    @Override
    public CompletableFuture<Void> connectAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        manager = new WebSocketConnectionManager(webSocketClient, handler, serverUri);
        manager.setAutoStartup(true);

        handler.setConnectedHandler(future::complete);
        manager.start();
        return future;
    }

    @Override
    public void disconnect() {
        manager.stop();
    }

    @Override
    public void sendMessage(WebSocketMessage<?> message) throws IOException {
        WebSocketSession session = handler.getSession();
        session.sendMessage(message);
    }


    @Override
    public CompletableFuture<WebSocketMessage<?>> receiveMessage() {
        CompletableFuture<WebSocketMessage<?>> future = new CompletableFuture<>();
        handler.setResponseHandler(future::complete);
        return future;
    }
}
