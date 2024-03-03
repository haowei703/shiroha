package com.shiroha.moyugongming.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Data
@RequiredArgsConstructor
public class User {
    // 用户id
    private int userId;
    // 用户名
    private String userName;
    // 电话
    @NonNull
    private String phoneNumber;
}
