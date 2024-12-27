package com.advent.aoc2024.utils;

public record CoordDelta(int x, int y) {
    public CoordDelta times(int times) {
        return new CoordDelta(this.x * times, this.y * times);
    }
}
