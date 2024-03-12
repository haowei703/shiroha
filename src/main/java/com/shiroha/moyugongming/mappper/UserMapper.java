package com.shiroha.moyugongming.mappper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroha.moyugongming.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
