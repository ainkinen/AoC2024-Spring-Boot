package com.advent.aoc2024.utils;

import org.apache.commons.lang3.IntegerRange;

public record Coord(int x, int y) {
    public CoordDelta deltaTo(Coord other) {
        return new CoordDelta(other.x - this.x, other.y - this.y);
    }

    public Coord plus(CoordDelta delta) {
        return new Coord(this.x + delta.x(), this.y + delta.y());
    }

    public Coord minus(CoordDelta delta) {
        return new Coord(this.x - delta.x(), this.y - delta.y());
    }

    public boolean inRange(IntegerRange range) {
        return range.contains(this.x) && range.contains(this.y);
    }
}
