package com.shiroha.moyugongming.controller;

import com.shiroha.moyugongming.entity.User;
import com.shiroha.moyugongming.service.Impl.RestServiceImpl;
import com.shiroha.moyugongming.service.Impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private RestServiceImpl restService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/getPhone")
    ResponseEntity<String> getPhone(@RequestBody String loginToken) {
        String phoneNumber = restService.resolveToken(loginToken);
        if (phoneNumber != null) {
            logger.info("获取的电话：" + phoneNumber);
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if(user == null){ // 新用户注册
                User newUser = new User(phoneNumber);
                StringBuilder username = new StringBuilder("用户");

                // 生成随机字母和数字的组合作为用户名的一部分
                Random random = new Random();
                String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
                int length = 6; // 设定用户名的长度为6，包括“用户”前缀

                for (int i = 0; i < length; i++) {
                    char randomChar = characters.charAt(random.nextInt(characters.length()));
                    username.append(randomChar);
                }

                // 使用随机用户名作为默认用户名
                newUser.setUserName(username.toString());
                userService.insertUser(newUser);
            }
            return ResponseEntity.ok(phoneNumber);
        } else {
            logger.error("token 解析失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("token解析失败");
        }
    }
}
