package com.avoris.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to run the Hotel Search Application.
 */
@SpringBootApplication(scanBasePackages = "com.avoris.app")
public class HotelSearchAppApplication {

	/**
	 * Main method to start the Spring Boot application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(HotelSearchAppApplication.class, args);
	}
}
