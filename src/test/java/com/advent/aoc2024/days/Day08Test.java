package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day08Test {

    private Day08 day;

    private final String testInput =
            """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day08();
    }

    @Test
    void part1() {
        assertEquals(14, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(34, day.part2(testInput));
    }
}
