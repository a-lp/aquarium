package fr.upem.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class WebApplication {
    public static String firstGenerationToken = "testingOnly";

    public static void main(String[] args) {
        firstGenerationToken = UUID.randomUUID().toString();
        System.out.println(firstGenerationToken);
        SpringApplication.run(WebApplication.class, args);
    }

}
