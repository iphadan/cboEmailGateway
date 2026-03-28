package com.coop.emailGateway.configurations;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Component
public class LogsDirectoryInitializer {

    @PostConstruct
    public void init() {
        try {
            Path logsPath = Paths.get("logs");
            if (!Files.exists(logsPath)) {
                Files.createDirectories(logsPath);
                System.out.println("#############################################################");
                System.out.println("# Created logs directory: " + logsPath.toAbsolutePath()+"#");
                System.out.println("#############################################################");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create logs directory", e);
        }
    }
}