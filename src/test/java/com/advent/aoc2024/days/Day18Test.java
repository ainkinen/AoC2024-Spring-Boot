package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.advent.aoc2024.utils.Bfs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day18Test {

    private Day18 day;

    private final String testInput =
            """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day18(new Bfs());
    }

    @Test
    void part1() {
        assertEquals(22, day.solverPart1(testInput, 12, 7));
    }

    @Test
    void part2() {
        assertEquals("6,1", day.part2Solver(testInput, 7));
    }
}
