package com.shiroha.moyugongming.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroha.moyugongming.entity.User;

import java.util.Map;


public interface UserService extends IService<User> {
    User findByPhoneNumber(String phoneNumber);
    void insertOne(String userName, String password, String phoneNumber);

    Map<String, String> getUserInfo(String phoneNumber);
}
