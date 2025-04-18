package com.kinopio.flourmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.kinopio.flourmo.*")
public class FlourMoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FlourMoApplication.class, args);
	}

}
