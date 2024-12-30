package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day19Test {

    private Day19 day;

    private final String testInput =
            """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day19();
    }

    @Test
    void part1() {
        assertEquals(6, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(16, day.part2(testInput));
    }
}
