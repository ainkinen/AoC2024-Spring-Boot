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

    public Coord mod(int max_x, int max_y) {
        return new Coord(((this.x % max_x) + max_x) % max_x, ((this.y % max_y) + max_y) % max_y);
    }

    public boolean inRange(IntegerRange range) {
        return range.contains(this.x) && range.contains(this.y);
    }

    public boolean inRange(IntegerRange x_range, IntegerRange y_range) {
        return x_range.contains(this.x) && y_range.contains(this.y);
    }
}
