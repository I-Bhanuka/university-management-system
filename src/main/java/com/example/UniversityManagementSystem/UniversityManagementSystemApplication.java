package com.example.UniversityManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class UniversityManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(UniversityManagementSystemApplication.class, args);
		log.info("UniversityManagementSystemApplication Server started");
		log.info("Server is running at http://localhost:8080");

	}

}
