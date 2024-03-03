package com.shiroha.moyugongming.service;
import com.shiroha.moyugongming.entity.User;

public interface UserService {
    User getUserByPhoneNumber(String phoneNumber);
    void insertUser(User user);
}
