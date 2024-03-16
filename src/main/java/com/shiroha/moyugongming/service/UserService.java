package com.shiroha.moyugongming.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroha.moyugongming.entity.User;

public interface UserService extends IService<User> {
    User findByPhoneNumber(String phoneNumber);
    boolean verifyPassword(String phoneNumber, String password);
    void insertOne(String userName, String password, String phoneNumber);
}
