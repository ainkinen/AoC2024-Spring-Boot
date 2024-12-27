package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day13Test {

    private Day13 day;

    private final String testInput =
            """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day13();
    }

    @Test
    void part1() {
        assertEquals(480, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(875318608908L, day.part2(testInput));
    }
}
