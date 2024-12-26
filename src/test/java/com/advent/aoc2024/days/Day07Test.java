package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day07Test {

    private Day07 day;

    private final String testInput =
            """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day07();
    }

    @Test
    void part1() {
        assertEquals(3749, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(11387, day.part2(testInput));
    }
}
