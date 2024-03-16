package com.shiroha.moyugongming.controller;

import com.shiroha.moyugongming.service.Impl.WebSocketClientServiceImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 客户端上传视频，服务端回传手语识别结果和语音播报
 */
@Component
@Slf4j
public class VideoSocketController extends BinaryWebSocketHandler {
    @Autowired
    WebSocketClientServiceImpl webSocketClientService;

    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage message) throws IOException, InterruptedException {

        // 异步建立连接，当连接成功后调用回调
        webSocketClientService.connectAsync().thenAccept(Void -> {
            try {
                // 转发消息给本地python WebSocket服务器
                webSocketClientService.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            CompletableFuture<WebSocketMessage<?>> future = webSocketClientService.receiveMessage();
            // 本地python WebSocket服务器的异步消息
            future.thenAccept(response -> {
                try {
                    session.sendMessage(new TextMessage(response.toString()));
                } catch (IOException e) {
                    log.error("Error sending message to client: {}", e.getMessage());
                }
            });
        });
    }


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        // 连接建立时执行任何初始化操作
        log.info("The connection is established,{}:{}", Objects.requireNonNull(session.getRemoteAddress()).getHostString(), session.getRemoteAddress().getPort());
        session.setBinaryMessageSizeLimit(52428800);
        session.setTextMessageSizeLimit(52428800);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        // 连接关闭时执行任何清理操作
        log.info("connect is closed:{},{},{}", System.currentTimeMillis(), status.getReason(), session.getBinaryMessageSizeLimit());
        webSocketClientService.disconnect();
    }

}
