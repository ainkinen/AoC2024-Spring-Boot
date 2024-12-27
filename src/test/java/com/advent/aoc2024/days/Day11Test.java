package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day11Test {

    private Day11 day;

    private final String testInput =
            """
            125 17
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day11();
    }

    @Test
    void part1() {
        assertEquals(55312, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(65601038650482L, day.part2(testInput));
    }
}
