package com.server.insta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InstaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstaApplication.class, args);
	}

}
