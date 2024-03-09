package com.shiroha.moyugongming.requests;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequest {
    String phoneNumber;
    String password;
    String code;
}
