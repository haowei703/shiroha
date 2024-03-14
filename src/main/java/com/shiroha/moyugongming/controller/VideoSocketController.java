package com.shiroha.moyugongming.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 客户端上传视频，服务端回传手语识别结果和语音播报
 */
@Component
@Slf4j
public class VideoSocketController extends BinaryWebSocketHandler {
    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session, BinaryMessage message) {
        // 处理接收到的二进制视频流
        byte[] videoData = message.getPayload().array();
        String path = "src/main/resources/video";
        Path directoryPath = Paths.get(path);

        // 检查目录是否存在，如果不存在则创建
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
                System.out.println("Directory created: " + directoryPath);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + directoryPath);
                e.printStackTrace();
                return;
            }
        }

        Path filePath = Paths.get(path, "test.mp4");

        // 将视频数据写入文件
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(videoData);
            System.out.println("Video saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save video to file: " + filePath);
            e.printStackTrace();
        }

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
    }

}
