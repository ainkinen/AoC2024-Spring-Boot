package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.advent.aoc2024.utils.Bfs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day10Test {

    private Day10 day;

    private final String testInput =
            """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day10(new Bfs());
    }

    @Test
    void part1() {
        assertEquals(36, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(81, day.part2(testInput));
    }
}
