package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day14Test {

    private Day14 day;

    private final String testInput =
            """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day14();
    }

    @Test
    void part1() {
        assertEquals(12, day.solver(testInput, 100, 11, 7));
    }

    @Test
    void part2() {
        assertEquals(24, day.solverPart2(testInput, 11, 7));
    }
}
