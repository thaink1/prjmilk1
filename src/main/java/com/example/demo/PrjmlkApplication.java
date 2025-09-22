package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repo")
@EntityScan(basePackages = "com.example.demo.model")
public class PrjmlkApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrjmlkApplication.class, args);
    }
}
