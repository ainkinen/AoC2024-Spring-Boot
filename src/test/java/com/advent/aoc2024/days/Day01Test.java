package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day01Test {

    private Day01 day;

    private final String testInput =
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;

    @BeforeEach
    void setUp() {
        day = new Day01();
    }

    @Test
    void part1() {
        assertEquals(11, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(31L, day.part2(testInput));
    }
}
