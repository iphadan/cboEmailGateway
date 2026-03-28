package com.coop.emailGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EmailGatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(EmailGatewayApplication.class, args);
	}

//	private static void createLogsDirectory() {
//		Path logsPath = Paths.get("logs");
//		if (!Files.exists(logsPath)) {
//			try {
//				Files.createDirectories(logsPath);
//				System.out.println("#############################################################");
//				System.out.println("# Created logs directory: " + logsPath.toAbsolutePath()+"#");
//				System.out.println("#############################################################");
//
//			} catch (Exception e) {
//				System.err.println("Failed to create logs directory: " + e.getMessage());
//			}
//		}
//	}


}
