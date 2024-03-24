package com.shiroha.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.57.2)",
        comments = "Source: MessageExchange.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MessageExchangeGrpc {

    private static final int METHODID_SEND_MESSAGE = 0;

    public static final java.lang.String SERVICE_NAME = "messageexchange.MessageExchange";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<com.shiroha.grpc.MessageRequest,
            com.shiroha.grpc.MessageResponse> getSendMessageMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "SendMessage",
            requestType = com.shiroha.grpc.MessageRequest.class,
            responseType = com.shiroha.grpc.MessageResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.shiroha.grpc.MessageRequest,
            com.shiroha.grpc.MessageResponse> getSendMessageMethod() {
        io.grpc.MethodDescriptor<com.shiroha.grpc.MessageRequest, com.shiroha.grpc.MessageResponse> getSendMessageMethod;
        if ((getSendMessageMethod = MessageExchangeGrpc.getSendMessageMethod) == null) {
            synchronized (MessageExchangeGrpc.class) {
                if ((getSendMessageMethod = MessageExchangeGrpc.getSendMessageMethod) == null) {
                    MessageExchangeGrpc.getSendMessageMethod = getSendMessageMethod =
                            io.grpc.MethodDescriptor.<com.shiroha.grpc.MessageRequest, com.shiroha.grpc.MessageResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendMessage"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.shiroha.grpc.MessageRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.shiroha.grpc.MessageResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new MessageExchangeMethodDescriptorSupplier("SendMessage"))
                                    .build();
                }
            }
        }
        return getSendMessageMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static MessageExchangeStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MessageExchangeStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<MessageExchangeStub>() {
                    @java.lang.Override
                    public MessageExchangeStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new MessageExchangeStub(channel, callOptions);
                    }
                };
        return MessageExchangeStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static MessageExchangeBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MessageExchangeBlockingStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<MessageExchangeBlockingStub>() {
                    @java.lang.Override
                    public MessageExchangeBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new MessageExchangeBlockingStub(channel, callOptions);
                    }
                };
        return MessageExchangeBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static MessageExchangeFutureStub newFutureStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MessageExchangeFutureStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<MessageExchangeFutureStub>() {
                    @java.lang.Override
                    public MessageExchangeFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new MessageExchangeFutureStub(channel, callOptions);
                    }
                };
        return MessageExchangeFutureStub.newStub(factory, channel);
    }

    /**
     *
     */
    public interface AsyncService {

        /**
         *
         */
        default void sendMessage(com.shiroha.grpc.MessageRequest request,
                                 io.grpc.stub.StreamObserver<com.shiroha.grpc.MessageResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service MessageExchange.
     */
    public static abstract class MessageExchangeImplBase
            implements io.grpc.BindableService, AsyncService {

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return MessageExchangeGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service MessageExchange.
     */
    public static final class MessageExchangeStub
            extends io.grpc.stub.AbstractAsyncStub<MessageExchangeStub> {
        private MessageExchangeStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MessageExchangeStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MessageExchangeStub(channel, callOptions);
        }

        /**
         *
         */
        public void sendMessage(com.shiroha.grpc.MessageRequest request,
                                io.grpc.stub.StreamObserver<com.shiroha.grpc.MessageResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service MessageExchange.
     */
    public static final class MessageExchangeBlockingStub
            extends io.grpc.stub.AbstractBlockingStub<MessageExchangeBlockingStub> {
        private MessageExchangeBlockingStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MessageExchangeBlockingStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MessageExchangeBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public com.shiroha.grpc.MessageResponse sendMessage(com.shiroha.grpc.MessageRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(
                    getChannel(), getSendMessageMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service MessageExchange.
     */
    public static final class MessageExchangeFutureStub
            extends io.grpc.stub.AbstractFutureStub<MessageExchangeFutureStub> {
        private MessageExchangeFutureStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MessageExchangeFutureStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MessageExchangeFutureStub(channel, callOptions);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<com.shiroha.grpc.MessageResponse> sendMessage(
                com.shiroha.grpc.MessageRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(
                    getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
        }
    }
    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final AsyncService serviceImpl;
        private final int methodId;

        MethodHandlers(AsyncService serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_SEND_MESSAGE:
                    serviceImpl.sendMessage((com.shiroha.grpc.MessageRequest) request,
                            (io.grpc.stub.StreamObserver<com.shiroha.grpc.MessageResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private MessageExchangeGrpc() {
    }

    private static abstract class MessageExchangeBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        MessageExchangeBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.shiroha.grpc.MessageExchangeProto.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("MessageExchange");
        }
    }

    private static final class MessageExchangeFileDescriptorSupplier
            extends MessageExchangeBaseDescriptorSupplier {
        MessageExchangeFileDescriptorSupplier() {
        }
    }

    private static final class MessageExchangeMethodDescriptorSupplier
            extends MessageExchangeBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final java.lang.String methodName;

        MessageExchangeMethodDescriptorSupplier(java.lang.String methodName) {
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                        getSendMessageMethod(),
                        io.grpc.stub.ServerCalls.asyncUnaryCall(
                                new MethodHandlers<
                                        com.shiroha.grpc.MessageRequest,
                                        com.shiroha.grpc.MessageResponse>(
                                        service, METHODID_SEND_MESSAGE)))
                .build();
    }

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (MessageExchangeGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new MessageExchangeFileDescriptorSupplier())
                            .addMethod(getSendMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
