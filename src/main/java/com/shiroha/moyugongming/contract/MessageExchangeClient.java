package com.shiroha.moyugongming.contract;

import com.google.protobuf.ByteString;
import com.shiroha.grpc.MessageExchangeGrpc;
import com.shiroha.grpc.MessageRequest;
import com.shiroha.grpc.MessageResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;


@Component
public class MessageExchangeClient {

    private final ManagedChannel channel;
    private final MessageExchangeGrpc.MessageExchangeBlockingStub blockingStub;

    public MessageExchangeClient() {
        channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 8765)
                .usePlaintext()
                .build();
        blockingStub = MessageExchangeGrpc.newBlockingStub(channel);
    }

    public String sendMessage(byte[] message) {
        MessageRequest request = MessageRequest
                .newBuilder()
                .setMessage(ByteString.copyFrom(message))
                .build();
        MessageResponse response = blockingStub.sendMessage(request);
        channel.shutdown();
        return response.getMessage();
    }
}
