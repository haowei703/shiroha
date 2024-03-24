package com.shiroha.moyugongming.grpc.client;

import com.google.protobuf.ByteString;
import com.shiroha.grpc.MessageExchangeGrpc;
import com.shiroha.grpc.MessageRequest;
import com.shiroha.grpc.MessageResponse;
import com.shiroha.moyugongming.response.GrpcResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MessageExchangeClient {

    @Value("${python.service.host}")
    private String host;

    @Value("${python.service.port}")
    private int port;


    public GrpcResponse sendMessage(byte[] binary_image, int width, int height) {
        MessageRequest request = MessageRequest
                .newBuilder()
                .setBinaryImage(ByteString.copyFrom(binary_image))
                .setWidth(width)
                .setHeight(height)
                .build();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        MessageExchangeGrpc.MessageExchangeBlockingStub blockingStub = MessageExchangeGrpc.newBlockingStub(channel);
        try {
            MessageResponse response = blockingStub.sendMessage(request);
            channel.shutdown();
            GrpcResponse grpcResponse = new GrpcResponse();
            grpcResponse.setResult(response.getResult());
            grpcResponse.setEmpty(response.getIsEmpty());
            return grpcResponse;
        } catch (StatusRuntimeException e) {
            log.error("gRPC failed:{}", e.getStatus());
            return null;
        }
    }
}
