package com.shiroha.moyugongming;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.shiroha.moyugongming.mappper")
@SpringBootApplication
public class MoyugongmingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyugongmingApplication.class, args);
    }

}
