package com.server.insta;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InstaApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml";

	public static void main(String[] args) {

		new SpringApplicationBuilder(InstaApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);

	}
}
