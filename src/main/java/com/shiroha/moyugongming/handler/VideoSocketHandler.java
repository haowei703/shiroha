package com.shiroha.moyugongming.handler;

import com.shiroha.moyugongming.config.redis.RedisCache;
import com.shiroha.moyugongming.grpc.client.MessageExchangeClient;
import com.shiroha.moyugongming.response.GrpcResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 客户端上传视频，服务端回传手语识别结果和语音播报
 */
@Component
@Slf4j
@ControllerAdvice
public class VideoSocketHandler extends BinaryWebSocketHandler {

    private final ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger messageCount = new AtomicInteger(0);
    @Autowired
    RedisCache redisCache;
    @Autowired
    private MessageExchangeClient messageExchangeClient;

    private void addToMessageQueue(String message) {
        messageQueue.offer(message);
    }

    // 合并消息并发送给客户端
    private void sendMergedMessage(WebSocketSession session) {
        StringBuilder mergedMessage = new StringBuilder();
        for (int i = 0; i < 3 && !messageQueue.isEmpty(); i++) {
            String message = messageQueue.poll();
            if (message != null) {
                mergedMessage.append(message);
            }
        }

        try {
            if (!mergedMessage.isEmpty()) {
                // 发送合并后的消息给客户端
                session.sendMessage(new TextMessage(mergedMessage.toString()));
            }
        } catch (IOException e) {
            log.error("Failed to send merged message: {}", e.getMessage());
        }
    }
    @Override
    public void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage message) throws Exception {
        if (message.getPayloadLength() <= 50) {
            setRedisCache(session, message);
            return;
        }
        try {
            Map<String, Integer> image_size = getSize(session);
            if (image_size != null) {
                byte[] bytes = message.getPayload().array();
                int width = image_size.get("width");
                int height = image_size.get("height");
                GrpcResponse response = messageExchangeClient.sendMessage(bytes, width, height);
                if (response != null && !response.isEmpty()) {
                    addToMessageQueue(response.getResult());

                    // 增加消息计数器
                    int count = messageCount.incrementAndGet();

                    // 当消息计数器达到3时，合并消息并发送给客户端
                    if (count >= 3) {
                        sendMergedMessage(session);
                        messageCount.set(0); // 重置消息计数器
                    }
                }
            } else {
                throw new Exception("image size is null!");
            }
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void setRedisCache(WebSocketSession session, BinaryMessage message) {
        // 缓存信息至内存池
        Map<String, Integer> map = new HashMap<>();
        String regex = "#width=(\\d+)&height=(\\d+)";

        Pattern pattern = Pattern.compile(regex);

        String decodedMessage = new String(message.getPayload().array(), StandardCharsets.UTF_8);
        Matcher matcher = pattern.matcher(decodedMessage);

        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));
            map.put("width", width);
            map.put("height", height);
            redisCache.setCacheMap(String.valueOf(session.getRemoteAddress()), map);
            redisCache.expire(String.valueOf(session.getRemoteAddress()), 1, TimeUnit.MINUTES);
        } else {
            log.warn("string format is incorrect");
        }
    }

    private Map<String, Integer> getSize(WebSocketSession session) {
        Map<String, Integer> map = redisCache.getCacheMap(String.valueOf(session.getRemoteAddress()));
        if (map.containsKey("width") && map.containsKey("height")) {
            int width = map.get("width");
            int height = map.get("height");
            Map<String, Integer> sizeMap = new HashMap<>();
            sizeMap.put("width", width);
            sizeMap.put("height", height);
            return sizeMap;

        } else return null;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        // 连接建立时执行任何初始化操作
        log.info("The connection is established,{}:{}", Objects.requireNonNull(session.getRemoteAddress()).getHostString(), session.getRemoteAddress().getPort());
        session.setBinaryMessageSizeLimit(524288000);
        session.setTextMessageSizeLimit(52428800);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        // 连接关闭时执行任何清理操作
        log.info("connect is closed:{},{},{}", System.currentTimeMillis(), status.getReason(), session.getBinaryMessageSizeLimit());
    }
}
