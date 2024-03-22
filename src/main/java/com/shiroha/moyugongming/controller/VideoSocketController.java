package com.shiroha.moyugongming.controller;

import com.shiroha.moyugongming.config.redis.RedisCache;
import com.shiroha.moyugongming.service.Impl.WebSocketClientServiceImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 客户端上传视频，服务端回传手语识别结果和语音播报
 */
@Component
@Slf4j
@ControllerAdvice
public class VideoSocketController extends BinaryWebSocketHandler {

    @Autowired
    WebSocketClientServiceImpl webSocketClientService;

    @Autowired
    RedisCache redisCache;


    @Override
    public void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage message) throws Exception {
        if (message.getPayloadLength() <= 50) {
            setRedisCache(session, message);
            return;
        }
        if (!webSocketClientService.isConnected()) {
            // 异步建立连接，当连接成功后调用回调
            webSocketClientService.connectAsync().thenAccept(Void -> {
                try {
                    byte[] image_size = getSize(session);
                    if (image_size != null) {
                        log.info(Arrays.toString(image_size));
                        byte[] bytes = message.getPayload().array();
                        int size = image_size.length + bytes.length;
                        byte[] new_bytes = new byte[size];
                        System.arraycopy(image_size, 0, new_bytes, 0, image_size.length);
                        System.arraycopy(bytes, 0, new_bytes, image_size.length, bytes.length);
                        log.info(String.valueOf(new_bytes.length));
                        BinaryMessage binaryMessage = new BinaryMessage(new_bytes);
                        sendMessage(session, binaryMessage);
                    } else {
                        throw new Exception("image size is null!");
                    }
                } catch (NullPointerException e) {
                    log.error(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        } else {
            byte[] image_size = getSize(session);
            if (image_size != null) {
                byte[] bytes = message.getPayload().array();
                int size = image_size.length + bytes.length;
                byte[] new_bytes = new byte[size];
                System.arraycopy(image_size, 0, new_bytes, 0, image_size.length);
                System.arraycopy(bytes, 0, new_bytes, image_size.length, bytes.length);
                log.info(String.valueOf(new_bytes.length));
                BinaryMessage binaryMessage = new BinaryMessage(new_bytes);
                sendMessage(session, binaryMessage);
            } else {
                throw new Exception("image size is null!");
            }
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            // 转发消息给本地python WebSocket服务器
            webSocketClientService.sendMessage(message).thenAccept(response -> {
                try {
                    session.sendMessage(new TextMessage(response.getPayload().toString()));
                } catch (IOException e) {
                    log.error("Error sending message to client: {}", e.getMessage());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setRedisCache(WebSocketSession session, BinaryMessage message) {
        // 缓存信息至内存池
        Map<String, Object> map = new HashMap<>();
        String regex = "#width=(\\d+)&height=(\\d+)";

        Pattern pattern = Pattern.compile(regex);

        String decodedMessage = new String(message.getPayload().array(), StandardCharsets.UTF_8);
        log.info(decodedMessage);
        Matcher matcher = pattern.matcher(decodedMessage);

        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));
            map.put("width", width);
            map.put("height", height);
            redisCache.setCacheMap(String.valueOf(session.getRemoteAddress()), map);
            redisCache.expire(String.valueOf(session.getRemoteAddress()), 30, TimeUnit.MINUTES);
        } else {
            log.warn("string format is incorrect");
        }
    }

    private byte[] getSize(WebSocketSession session) {
        Map<String, Object> map = redisCache.getCacheMap(String.valueOf(session.getRemoteAddress()));
        if (map.containsKey("width") && map.containsKey("height")) {
            int width = (Integer) map.get("width");
            int height = (Integer) map.get("height");

            ByteBuffer bb = ByteBuffer.allocate(8);
            bb.putInt(width);
            bb.putInt(height);

            return bb.array();
        } else return null;
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
