package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day01Test {

    private final Day01 day01 = new Day01();

    private final String testInput =
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;

    @Test
    void part1() {
        assertEquals(11, day01.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(31L, day01.part2(testInput));
    }
}
