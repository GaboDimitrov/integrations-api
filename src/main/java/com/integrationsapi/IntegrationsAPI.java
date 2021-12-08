package com.integrationsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class IntegrationsAPI {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationsAPI.class, args);
	}

}
