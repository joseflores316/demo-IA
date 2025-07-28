package com.jose.demoia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoIaApplication {

    public static void main(String[] args) {
        System.out.println("=== Iniciando aplicación con SpringDoc OpenAPI ===");
        SpringApplication.run(DemoIaApplication.class, args);
    }
}
