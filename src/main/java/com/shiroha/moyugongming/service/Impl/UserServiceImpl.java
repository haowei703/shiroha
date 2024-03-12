package com.shiroha.moyugongming.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroha.moyugongming.entity.User;
import com.shiroha.moyugongming.mappper.UserMapper;
import com.shiroha.moyugongming.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserDetailsService, UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhoneNumber, phoneNumber);
        try {
            return userMapper.selectOne(wrapper);
        } catch (Exception e) {
            log.error("user is not find");
            return null;
        }
    }
    @Override
    public User findByPhoneNumber(String phoneNumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phoneNumber", phoneNumber);
        return userMapper.selectOne(queryWrapper);
    }
    @Override
    public boolean verifyPassword(String phoneNumber, String password) {
        User user = findByPhoneNumber(phoneNumber);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    @Override
    public void insertOne(String userName, String password, String phoneNumber) {
        try {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhoneNumber, phoneNumber);
            if (userMapper.selectList(wrapper).isEmpty()) {
                User user = new User();
                user.setUserName(userName);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                userMapper.insert(user);
            } else {
                log.error("user already exists");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
