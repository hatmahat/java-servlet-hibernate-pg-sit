package com.example.java_spring_hibernate_pg_sit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class JavaSpringHibernatePgSitApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaSpringHibernatePgSitApplication.class, args);
	}

}
