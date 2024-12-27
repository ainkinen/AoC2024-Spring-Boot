package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day09Test {

    private Day09 day;

    private final String testInput = "2333133121414131402";

    @BeforeEach
    void setUp() {
        this.day = new Day09();
    }

    @Test
    void part1() {
        assertEquals(1928, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(2858, day.part2(testInput));
    }
}
