package com.ayush.end_to_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ayush.end_to_end",
    "service",
    "service.serviceImpl",
    "userRest",
    "rest.impl"
})
@EntityScan("com.ayush.end_to_end.entity")
@EnableJpaRepositories("com.ayush.end_to_end.repository")
public class EndToEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(EndToEndApplication.class, args);
	}

}
