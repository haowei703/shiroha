package com.shiroha.moyugongming.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class HttpUtil {
    /**
     * 解析请求的参数
     *
     * @param queryParams
     * @param decodeMethod 解码方法
     * @return hashmap
     */
    static public @NonNull HashMap<String, String> decodeParamMap(@NonNull String queryParams, @NonNull String decodeMethod) {
        HashMap<String, String> paramMap = new HashMap<>();

        try {
            String decodedParams = URLDecoder.decode(queryParams, decodeMethod);
            String[] paramPairs = decodedParams.split("&");

            for (String pair : paramPairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];

                    paramMap.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return paramMap;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
