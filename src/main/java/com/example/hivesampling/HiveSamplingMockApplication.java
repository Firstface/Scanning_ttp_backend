package com.example.hivesampling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HiveSamplingMockApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiveSamplingMockApplication.class, args);
    }
}
