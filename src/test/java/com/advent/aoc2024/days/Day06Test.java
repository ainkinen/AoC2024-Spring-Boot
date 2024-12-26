package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day06Test {

    private Day06 day;

    private final String testInput =
            """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day06();
    }

    @Test
    void part1() {
        assertEquals(41, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(6, day.part2(testInput));
    }
}
