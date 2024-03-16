package com.shiroha.moyugongming.service;


public interface RedisService {
    void setValue(String key, String value);
    String getValue(String key);
    boolean verifyCode(String phoneNumber, String code);
}
