package com.shiroha.moyugongming.controller;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

/**
 * 客户端上传视频，服务端回传手语识别结果和语音播报
 */
@Component
public class VideoSocketController extends BinaryWebSocketHandler {
    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session, BinaryMessage message) {
        // 在此处处理接收到的二进制消息（视频流）
        byte[] videoData = message.getPayload().array();

        // 根据需要执行处理或保存视频数据

        // 如果需要，还可以向客户端发送响应
        // 例如，将接收到的数据回传给客户端：
        try {
            session.sendMessage(new BinaryMessage(videoData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        // 连接建立时执行任何初始化操作
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        // 连接关闭时执行任何清理操作
    }

}
