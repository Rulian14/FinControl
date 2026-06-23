package com.fincontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class FincontrolApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FincontrolApiApplication.class, args);
	}
}
