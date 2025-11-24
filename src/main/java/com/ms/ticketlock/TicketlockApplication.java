package com.ms.ticketlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TicketlockApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketlockApplication.class, args);
	}

}
