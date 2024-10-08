package com.tiss.vitagergateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class VitagerProperties {
    @Value("${spring.service.vitager.address.host}")
    private String host;
    @Value("${spring.service.vitager.address.token}")
    private String token;
    @Value("${spring.service.vitager.address.username}")
    private String username;
    @Value("${spring.service.vitager.address.password}")
    private String password;
    @Value("${spring.service.vitager.address.client_secret}")
    private String client_secret;
    @Value("${spring.service.vitager.address.client_id}")
    private String client_id;
}
