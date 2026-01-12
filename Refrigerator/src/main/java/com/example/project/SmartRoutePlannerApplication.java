package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync      // @Async 활성화
@EnableScheduling // @Scheduled 활성화
@SpringBootApplication
public class SmartRoutePlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartRoutePlannerApplication.class, args);
	}

	
}
