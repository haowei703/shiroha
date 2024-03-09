package com.shiroha.moyugongming.controller;

import com.shiroha.moyugongming.entity.User;
import com.shiroha.moyugongming.requests.LoginRequest;
import com.shiroha.moyugongming.service.Impl.RedisServiceImpl;
import com.shiroha.moyugongming.service.Impl.UserServiceImpl;
import com.shiroha.moyugongming.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;


@RestController
@RequestMapping("/authentication")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/sendSMS")
    ResponseEntity<String> sendSMS(@RequestParam String phoneNumber) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        String code = str.toString();
        try {
            ResponseEntity<String> result = HttpClientUtils.SendSMSCode(phoneNumber, code);
            if (result.getStatusCode() == HttpStatus.OK) {
                try {
                    redisService.setValue(phoneNumber, code);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return ResponseEntity.badRequest().body("验证码插入失败");
                }
                return ResponseEntity.ok("验证码发送成功,验证码" + code);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("验证码发送失败");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("手机号为空");
    }

    @PostMapping("/login")
    ResponseEntity<String> Login(@RequestBody LoginRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String code = request.getCode();
        String password = request.getPassword();
        if (phoneNumber != null && code != null) {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if(user != null){
                if (redisService.getValue(phoneNumber).equals(code)) {
                    return ResponseEntity.ok("登录成功");
                } else return ResponseEntity.badRequest().body("验证码已过期");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该用户还未注册");
            }
        } else if (phoneNumber != null && password != null) {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if (user.getPassword().equals(password)) {
                return ResponseEntity.ok("登录成功");
            } else return ResponseEntity.badRequest().body("用户名或密码错误");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("api调用错误");
    }

    @PostMapping("register")
    ResponseEntity<String> Register(@RequestBody LoginRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String code = request.getCode();
        String password = request.getPassword();
        if (redisService.getValue(phoneNumber).equals(code)) {
            User user = getUser(phoneNumber, password);
            logger.info("新用户插入" + user);
            User user1 = userService.getUserByPhoneNumber(phoneNumber);
            if(user1 == null) {
                userService.insertUser(user);
                return ResponseEntity.ok("注册成功");
            } else  {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("该手机号已注册");
            }
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("验证码已过期");
        }

    }

    private static User getUser(String phoneNumber, String password) {
        User user = new User();

        // 生成随机用户名
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String USERNAME_PREFIX = "用户";
        int USERNAME_LENGTH = 8;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(USERNAME_LENGTH);
        for (int i = 0; i < USERNAME_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        sb.insert(0, USERNAME_PREFIX);

        user.setUserName(sb.toString());
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}
