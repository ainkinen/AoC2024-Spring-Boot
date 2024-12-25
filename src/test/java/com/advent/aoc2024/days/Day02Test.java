package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day02Test {

    private Day02 day;

    private final String testInput =
            """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day02();
    }

    @Test
    void part1() {
        assertEquals(2, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(4, day.part2(testInput));
    }
}
