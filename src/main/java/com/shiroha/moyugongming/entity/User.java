package com.shiroha.moyugongming.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@TableName("user")
public class User implements UserDetails {


    @TableId(type = IdType.AUTO)
    private Long id;
    // 用户名
    @Getter
    @TableField("userName")
    private String userName;
    // 密码
    @TableField("password")
    private String password;
    // 电话
    @TableField("phoneNumber")
    private String phoneNumber;
    // 权限
    @TableField("role")
    private String role;

    @Override//用户所拥有的权限
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override//实现UserDetails的getPassword方法，返回实体类的password
    public String getPassword() {
        return password;
    }

    @Override//UserDetails中的方法
    public String getUsername() {
        // 使用手机号作为登录凭据
        return phoneNumber;
    }

    @Override//返回true，代表用户账号没过期
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override//返回true，代表用户账号没被锁定
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override//返回true，代表用户密码没有过期
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override//返回true，代表用户账号还能够使用
    public boolean isEnabled() {
        return true;
    }
}

