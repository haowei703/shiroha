package com.shiroha.moyugongming.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.shiroha.moyugongming.service.RestService;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * @param loginToken 登录令牌
     * @return 获取的手机号
     */
    @Override
    public String resolveToken(String loginToken) throws RestClientException, JSONException {


        String prikey = """
                MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALy1Sm8znAZaaRzL
                5nD8lVYrHyRpBj0f+Hz68WYHwluU7lS3KpNlq8IK0wKJ1y0jSESgnSxNnbPz18Ss
                JLXBVjyHTr68C57lgOSS+4LRx+1+zjx1Vxy55tXl7YRNT3w+SaCgzD88Npyn90Gz
                ly1OKORHKGbiRDQsARvmImj8c3iRAgMBAAECgYApyffW0lX97i2Sy/CXGsgzUc9O
                r9dK1/aeLrAgiR1YTI3OwtUx2PUFPoJl8IJvI34QfITx16pRvHEOw5RJj6v5z9R7
                5I9sWZ1c4/ZKgoNomEVvyWNCB0H+b2s6GPJo1JxemNZ8n5OmdRRhH8193qr1v2do
                wJ5hE26vGTuzcxb15QJBAO2ZiM3Zye3hMqak21OrxAHH6ggwml8oUj+p/FnaOvuf
                DRuvJul2d6QAw7Xi65PR4oB84/vcQtalJtsfzZcEqK8CQQDLUnhuF5GIBk/XtwJ/
                XSS/wPsLSVbvVncQuSio/kyMm2KVsXOWka6Sa9NYPHA48MePTBjnleYMLYDfJQbp
                a8K/AkEApZshFlGZhFs1gZ9gW+ZZ3N/piEHQJ0kkYslpYXtRJzaJ3WTsm0b+Rupd
                fvYmOsbMt7/U7CRu1csQ1SRKmNsfcwJBALRrkzhGK7rjtvf5ivrQAWsy6NJEGyO4
                u2EYnQ8Q0Ya338xrseE7lwiRK/KRdxff+YWcBkGCj6fgvQnGRpLdL+sCQQDHbaSj
                c1exrsSsfwAlhp7JIOZI7dv8s4564TGUzNCl86gDELZCklF8C5w12MhwvCGarCZB
                M8NuJUXdzPKs2Z7J""";

        String url = "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
        String appKey = "87f1b6427c5884046513d8f4";
        String Secret = "15d0006a72f2567b190d21b4";

        Map<String, String> map = new HashMap<>();
        map.put("loginToken", loginToken);
        map.put("exID", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((appKey + ":" + Secret).getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            String result = response.getBody();
            System.out.println("result:[{}] " + result);

            JSONObject jsonObject = JSON.parseObject(result);
            System.out.println("获取phone:{} " + JSON.toJSON(jsonObject));
            String cryptograph_phone = (String) jsonObject.get("phone");

            if (StringUtils.isBlank(cryptograph_phone)) {
                throw new RuntimeException("极光认证出错");
            }

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] b = Base64.getDecoder().decode(cryptograph_phone);

            return new String(cipher.doFinal(b));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HTTP 请求出错");
        }
    }
}
