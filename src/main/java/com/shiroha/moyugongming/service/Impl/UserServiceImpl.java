package com.shiroha.moyugongming.service.Impl;

import com.shiroha.moyugongming.dao.UserMapper;
import com.shiroha.moyugongming.entity.User;
import com.shiroha.moyugongming.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 根据电话号码返回对应的用户
     * @param phoneNumber 电话号码
     * @return 查找到的用户
     */
    @Override
    public  User getUserByPhoneNumber(String phoneNumber) {
        return userMapper.getUserByPhoneNumber(phoneNumber);
    }

    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }
}
