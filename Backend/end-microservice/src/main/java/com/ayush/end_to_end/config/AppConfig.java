package com.ayush.end_to_end.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {
    "com.ayush.end_to_end",
    "service",
    "service.serviceImpl",
    "userRest",
    "rest.impl"
})
@EntityScan("com.ayush.end_to_end.entity")
@EnableJpaRepositories("com.ayush.end_to_end.repository")
public class AppConfig {
    
} 