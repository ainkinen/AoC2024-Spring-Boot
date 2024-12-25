package com.advent.aoc2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AoC2024 {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AoC2024.class, args);
        AoCExecutor executor = context.getBean(AoCExecutor.class);
        executor.run();
    }
}
