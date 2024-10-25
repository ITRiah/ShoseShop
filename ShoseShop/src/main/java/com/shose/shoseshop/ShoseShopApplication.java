package com.shose.shoseshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShoseShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShoseShopApplication.class, args);
	}
}
