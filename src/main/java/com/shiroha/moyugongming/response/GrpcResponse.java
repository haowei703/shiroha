package com.shiroha.moyugongming.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GrpcResponse {
    String result;
    boolean isEmpty;
}
