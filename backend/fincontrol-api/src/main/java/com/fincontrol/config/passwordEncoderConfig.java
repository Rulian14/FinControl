package com.fincontrol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class passwordEncoderConfig {
    @Bean
    public BCryptPasswordEncoder encode(){
        return new BCryptPasswordEncoder(10);
    }
}
