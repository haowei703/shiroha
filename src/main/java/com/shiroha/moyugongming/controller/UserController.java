package com.shiroha.moyugongming.controller;

import com.alibaba.fastjson2.JSON;
import com.shiroha.moyugongming.entity.User;
import com.shiroha.moyugongming.requests.LoginRequest;
import com.shiroha.moyugongming.service.Impl.RedisServiceImpl;
import com.shiroha.moyugongming.service.Impl.UserServiceImpl;
import com.shiroha.moyugongming.utils.HttpClientUtils;
import com.shiroha.moyugongming.utils.JwtUtils;
import com.shiroha.moyugongming.utils.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;


@RestController
@Slf4j
@RequestMapping("/user/")
public class UserController {

    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private UserServiceImpl userService;

    // 生成随机用户名
    private static String genUsername() {

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

        return sb.toString();
    }

    @GetMapping("sendSMS")
    public ResponseEntity<Result> sendSMS(@RequestParam String phoneNumber) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        String code = str.toString();
        try {
            ResponseEntity<String> result = HttpClientUtils.SendSMSCode(phoneNumber, code);
            if (result.getStatusCode() == HttpStatus.OK) {
                redisService.setValue(phoneNumber, code);
                return ResponseEntity.ok(Result.ok("验证码发送成功"));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Result.error("验证码发送失败，请联系客服反馈"));
        }

        return ResponseEntity.badRequest().body(Result.error("手机号为空"));
    }

    @PostMapping("login")
    public ResponseEntity<Result> Login(@RequestBody LoginRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String code = request.getCode();
        String password = request.getPassword();

        // 检查用户是否存在
        User user = userService.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.badRequest().body(Result.error("用户未注册"));
        }


        // 使用验证码登录，redis检验验证码正确则直接签发token
        if (code != null && password == null) {
            if (redisService.getValue(phoneNumber).equals(code)) {
                String token = JwtUtils.genAccessToken(phoneNumber);
                return ResponseEntity.ok(Result.ok("登录成功").setData(token));
            } else return ResponseEntity.badRequest().body(Result.error("验证码错误"));
        }

        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(phoneNumber, password);
            Authentication authentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = JwtUtils.genAccessToken(userDetails.getUsername());
            return ResponseEntity.ok(Result.ok("登录成功").setData(token));
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("用户名或密码不正确");
            return ResponseEntity.badRequest().body(Result.error("登录失败"));
        }
    }

    @PostMapping("register")
    Result Register(@RequestBody LoginRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String code = request.getCode();
        String password = request.getPassword();

        User user = userService.findByPhoneNumber(phoneNumber);
        if (user != null) {
            return Result.error("该手机号已注册");
        }

        if (redisService.getValue(phoneNumber).equals(code)) {
            String username = genUsername();
            String encoded = passwordEncoder.encode(password);
            userService.insertOne(username, encoded, phoneNumber);
            String token = JwtUtils.genAccessToken(phoneNumber);
            return Result.ok("注册成功").setData(token);
        } else {
            return Result.error("验证码已过期");
        }
    }


    @GetMapping("getUserInfo")
    public ResponseEntity<String> getUserInfo(@RequestParam String phoneNumber) {
        try {
            Map<String, String> userInfo = userService.getUserInfo(phoneNumber);
            String jsonStr = JSON.toJSONString(userInfo);
            return ResponseEntity.ok(jsonStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("bad request");
        }
    }
}
