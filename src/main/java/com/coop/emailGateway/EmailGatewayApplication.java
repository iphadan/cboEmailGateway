package com.coop.emailGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EmailGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailGatewayApplication.class, args);
	}

}
