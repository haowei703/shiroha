// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MessageExchange.proto

package com.shiroha.grpc;

public final class MessageExchangeProto {
    static {
        java.lang.String[] descriptorData = {
                "\n\025MessageExchange.proto\022\017messageexchange" +
                        "\"E\n\016MessageRequest\022\024\n\014binary_image\030\001 \001(\014" +
                        "\022\r\n\005width\030\002 \001(\005\022\016\n\006height\030\003 \001(\005\"2\n\017Messa" +
                        "geResponse\022\016\n\006result\030\001 \001(\t\022\017\n\007isEmpty\030\002 " +
                        "\001(\0102e\n\017MessageExchange\022R\n\013SendMessage\022\037." +
                        "messageexchange.MessageRequest\032 .message" +
                        "exchange.MessageResponse\"\000B*\n\020com.shiroh" +
                        "a.grpcB\024MessageExchangeProtoP\001b\006proto3"
        };
        descriptor = com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                        });
        internal_static_messageexchange_MessageRequest_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_messageexchange_MessageRequest_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_messageexchange_MessageRequest_descriptor,
                new java.lang.String[]{"BinaryImage", "Width", "Height",});
        internal_static_messageexchange_MessageResponse_descriptor =
                getDescriptor().getMessageTypes().get(1);
        internal_static_messageexchange_MessageResponse_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_messageexchange_MessageResponse_descriptor,
                new java.lang.String[]{"Result", "IsEmpty",});
    }

    private MessageExchangeProto() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistryLite registry) {
    }

    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_messageexchange_MessageRequest_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_messageexchange_MessageRequest_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_messageexchange_MessageResponse_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_messageexchange_MessageResponse_fieldAccessorTable;

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
        registerAllExtensions(
                (com.google.protobuf.ExtensionRegistryLite) registry);
    }

    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    // @@protoc_insertion_point(outer_class_scope)
}
