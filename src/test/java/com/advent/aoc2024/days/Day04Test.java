package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day04Test {

    private Day04 day;

    private final String testInput =
            """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day04();
    }

    @Test
    void part1() {
        assertEquals(18, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(9, day.part2(testInput));
    }
}
