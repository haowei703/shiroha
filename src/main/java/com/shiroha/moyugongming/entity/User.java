package com.shiroha.moyugongming.entity;

import lombok.*;

@Getter
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    private Integer userID;
    // 用户名
    private String userName;
    // 密码
    private String password;
    // 电话
    @NonNull
    private String phoneNumber;
}
