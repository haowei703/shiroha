package com.shiroha.moyugongming.handler;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.Consumer;


@Component
@Slf4j
public class WebSocketClientHandler implements WebSocketHandler {

    @Setter
    private Consumer<WebSocketMessage<?>> responseHandler;

    @Setter
    private Consumer<Void> connectedHandler;

    @Getter
    private WebSocketSession session;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        this.session = session;
        if (connectedHandler != null) {
            connectedHandler.accept(null);
        }
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        // 回传python响应信息
        if (responseHandler != null) {
            responseHandler.accept(message);
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        this.session = null;
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
