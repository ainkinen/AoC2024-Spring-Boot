package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;

import org.springframework.stereotype.Component;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Component
public class Day03 implements Day {

    private final Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
    private final Pattern complex_pattern =
            Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");

    @Override
    public Long part1(String input) {
        return pattern.matcher(input)
                .results()
                .mapToLong(r -> Long.parseLong(r.group(1)) * Long.parseLong(r.group(2)))
                .sum();
    }

    @Override
    public Long part2(String input) {

        boolean multiplying = true;
        long total = 0;

        for (MatchResult r : complex_pattern.matcher(input).results().toList()) {
            if (r.group(0).equals("do()")) {
                multiplying = true;
            } else if (r.group(0).equals("don't()")) {
                multiplying = false;
            } else if (multiplying) {
                long newValue = Long.parseLong(r.group(1)) * Long.parseLong(r.group(2));
                total += newValue;
            }
        }

        return total;
    }
}
