package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day12Test {

    private Day12 day;

    private final String testInputOne =
            """
            AAAA
            BBCD
            BBCC
            EEEC
            """;

    private final String testInputTwo =
            """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
            """;

    private final String testInputThree =
            """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day12();
    }

    @Test
    void part1() {
        assertEquals(140, day.part1(testInputOne));
        assertEquals(772, day.part1(testInputTwo));
        assertEquals(1930, day.part1(testInputThree));
    }

    @Test
    void part2() {
        assertEquals(80, day.part2(testInputOne));
        assertEquals(436, day.part2(testInputTwo));
        assertEquals(1206, day.part2(testInputThree));
    }
}
