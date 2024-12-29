package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day17Test {

    private Day17 day;

    private final String testInputOne =
            """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
            """;

    private final String testInputTwo =
            """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day17();
    }

    @Test
    void part1() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", day.part1(testInputOne));
    }

    @Test
    void part2Brute() {
        assertEquals(117440, day.part2Brute(testInputTwo));
    }

    @Test
    void part2() {
        assertEquals(117440, day.part2Brute(testInputTwo));
    }
}
