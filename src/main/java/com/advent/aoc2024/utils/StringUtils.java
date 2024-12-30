package com.advent.aoc2024.utils;

import java.util.Optional;

public class StringUtils {
    public static Optional<String> stripPrefix(String string, String prefix) {
        if (string != null && string.startsWith(prefix)) {
            return Optional.of(string.split(prefix, 2)[1]);
        }
        return Optional.empty();
    }
}
