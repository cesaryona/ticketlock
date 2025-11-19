package com.ms.ticketlock;

import org.springframework.boot.SpringApplication;

public class TestTicketlockApplication {

	public static void main(String[] args) {
		SpringApplication.from(TicketlockApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
