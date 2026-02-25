package com.uber.motoristauber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.uber.motoristauber", "com.motoristauber"})
@EntityScan(basePackages = {"com.uber.motoristauber.model", "com.motoristauber.model"})
@EnableJpaRepositories(basePackages = {"com.uber.motoristauber.repository", "com.motoristauber.repository"})
public class MotoristauberApplication {
    public static void main(String[] args) {
        SpringApplication.run(MotoristauberApplication.class, args);
    }
}
