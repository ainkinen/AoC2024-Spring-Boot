package com.advent.aoc2024.utils;

import org.apache.commons.lang3.IntegerRange;

import java.util.Objects;

public record Location(int x, int y, Direction dir) {

    public Location(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = Objects.requireNonNullElse(dir, Direction.up);
    }

    public Location turnBy(int degrees) {
        Direction dir = Direction.getByValue((this.dir.value + degrees) % 360).orElseThrow();
        return new Location(this.x, this.y, dir);
    }

    public Location step(CoordDelta delta) {
        int x = this.x + delta.x();
        int y = this.y + delta.y();

        return new Location(x, y, this.dir);
    }

    public Location step() {
        CoordDelta delta = this.dir.asCoordDelta();
        return step(delta);
    }

    public Coord getCoord() {
        return new Coord(this.x, this.y);
    }

    public boolean inRange(IntegerRange x_range, IntegerRange y_range) {
        return x_range.contains(this.x) && y_range.contains(this.y);
    }
}
