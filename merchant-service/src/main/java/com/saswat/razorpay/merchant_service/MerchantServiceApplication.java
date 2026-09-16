package com.saswat.razorpay.merchant_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class MerchantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchantServiceApplication.class, args);
	}

}
