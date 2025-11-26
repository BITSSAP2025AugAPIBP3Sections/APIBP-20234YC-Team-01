package com.example.GreenGrub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GreenGrubApplication {
	public static void main(String[] args) {
		SpringApplication.run(GreenGrubApplication.class, args);
	}
}
