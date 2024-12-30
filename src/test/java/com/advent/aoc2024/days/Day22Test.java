package com.advent.aoc2024.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class Day22Test {

    private Day22 day;

    private final String testInput =
            """
            1
            10
            100
            2024
            """;

    private final String testInputTwo =
            """
            1
            2
            3
            2024
            """;

    @BeforeEach
    void setUp() {
        this.day = new Day22();
    }

    @Test
    void testSecretNumberGenerator() {
        Day22.SecretNumberGenerator gen = new Day22.SecretNumberGenerator(BigInteger.valueOf(123));
        assertEquals(BigInteger.valueOf(15887950), gen.next());
        assertEquals(BigInteger.valueOf(16495136), gen.next());
        assertEquals(BigInteger.valueOf(527345), gen.next());
        assertEquals(BigInteger.valueOf(704524), gen.next());
        assertEquals(BigInteger.valueOf(1553684), gen.next());
        assertEquals(BigInteger.valueOf(12683156), gen.next());
        assertEquals(BigInteger.valueOf(11100544), gen.next());
        assertEquals(BigInteger.valueOf(12249484), gen.next());
        assertEquals(BigInteger.valueOf(7753432), gen.next());
        assertEquals(BigInteger.valueOf(5908254), gen.next());
    }

    @Test
    void part1() {
        assertEquals(37327623, day.part1(testInput));
    }

    @Test
    void part2() {
        assertEquals(23, day.part2(testInputTwo));
    }
}
