package com.shiroha.moyugongming.utils;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class HttpClientUtils {
    static public ResponseEntity<String> SendSMSCode(String phoneNumber, String code) {
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String appcode = "e5346af28c5e46e3bc25e77c1b1ed1bf";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "APPCODE " + appcode);

        // 设置请求体参数
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("content", String.format("code:%d", Integer.parseInt(code)));
        parameters.add("template_id", "CST_ptdie100");
        parameters.add("phone_number", phoneNumber);

        // 创建请求实体
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(host + path, requestEntity, String.class);
            if(response.getStatusCode().equals(HttpStatus.OK)){
                return ResponseEntity.ok("success");
            }else{
                return ResponseEntity.status(response.getStatusCode()).body("error:" + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            HttpStatusCode errorCode = e.getStatusCode();
            return ResponseEntity.badRequest().body("fail:"+ errorCode);
        }
    }
}