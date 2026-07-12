package com.transitops.emergency_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD

@SpringBootApplication
=======
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
>>>>>>> fb5e013 (Updated service APIs)
public class EmergencyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergencyServiceApplication.class, args);
	}

}
