package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day21Test {

    private Day21 day;

    private final String testInput =
            """
            029A
            980A
            179A
            456A
            379A
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day21();
    }

    @Test
    void part1() {
        assertEquals(126384, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(154115708116294L, day.part2(testInput));
    }
}
