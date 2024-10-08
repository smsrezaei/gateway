package com.tiss.vitagergateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class VitagergatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitagergatewayApplication.class, args);
    }

}
