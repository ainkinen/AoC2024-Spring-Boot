package com.advent.aoc2024;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InputReader {
    // TODO: Make configurable
    private static final String INPUT_DIRECTORY = "input";

    public String read(String day) {
        Path filePath = Paths.get(INPUT_DIRECTORY, day.toLowerCase() + ".txt");
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read file: " + filePath, e);
        }
    }
}
