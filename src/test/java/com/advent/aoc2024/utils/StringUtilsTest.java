package com.advent.aoc2024.utils;

import static com.advent.aoc2024.utils.StringUtils.stripPrefix;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Optional;

class StringUtilsTest {
    @Test
    void testStripPrefix() {
        assertEquals(Optional.of("bar"), stripPrefix("foobar", "foo"));
        assertEquals(Optional.of(""), stripPrefix("foo", "foo"));
        assertEquals(Optional.empty(), stripPrefix("mismatch", "foo"));
    }
}
