package com.shiroha.moyugongming.dao;

import com.shiroha.moyugongming.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User getUserByPhoneNumber(String phoneNumber);
    void insertUser(User user);
}
