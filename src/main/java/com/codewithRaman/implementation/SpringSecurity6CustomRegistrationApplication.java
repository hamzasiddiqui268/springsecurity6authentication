package com.codeWithRaman.implementation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.codeWithRaman.implementation")
public class SpringSecurity6CustomRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurity6CustomRegistrationApplication.class, args);
	}

}
