package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day25Test {

    private Day25 day;

    @BeforeEach
    void setUp() {
        this.day = new Day25();
    }

    @Test
    void part1() {
        String testInput =
                """
                #####
                .####
                .####
                .####
                .#.#.
                .#...
                .....

                #####
                ##.##
                .#.##
                ...##
                ...#.
                ...#.
                .....

                .....
                #....
                #....
                #...#
                #.#.#
                #.###
                #####

                .....
                .....
                #.#..
                ###..
                ###.#
                ###.#
                #####

                .....
                .....
                .....
                #....
                #.#..
                #.#.#
                #####
                """;
        assertEquals(3, day.part1(testInput));
    }
}
