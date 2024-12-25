package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day03Test {

    private Day03 day;

    @BeforeEach
    void setUp() {
        this.day = new Day03();
    }

    @Test
    void part1() {
        String testInput =
                "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
        assertEquals(161, day.part1(testInput));
    }

    @Test
    void part2() {
        String testInput2 =
                "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";
        assertEquals(48, day.part2(testInput2));
    }
}
