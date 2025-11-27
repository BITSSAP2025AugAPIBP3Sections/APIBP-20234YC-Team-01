package com.example.GreenGrub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class GreenGrubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenGrubApplication.class, args);
        log.info("Welcome to GreenGrub");
    }
}
