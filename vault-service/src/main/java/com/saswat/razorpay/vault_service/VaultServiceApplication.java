package com.saswat.razorpay.vault_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class VaultServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaultServiceApplication.class, args);
	}

}
