package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.advent.aoc2024.utils.Bfs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day20Test {

    private Day20 day;

    private final String testInput =
            """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day20(new Bfs());
    }

    @Test
    void part1() {
        assertEquals(44, day.solver(testInput, 2, 2));
    }

    @Test
    void part2() {
        assertEquals(285, day.solver(testInput, 50, 20));
    }
}
