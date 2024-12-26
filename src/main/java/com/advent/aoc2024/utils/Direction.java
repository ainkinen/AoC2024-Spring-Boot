package com.advent.aoc2024.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Direction {
    up(0),
    right(90),
    down(180),
    left(270);

    private static final Map<Integer, Direction> lookup = new HashMap<>();

    static {
        for (Direction direction : Direction.values()) {
            lookup.put(direction.value, direction);
        }
    }

    public final int value;

    Direction(int value) {
        this.value = value;
    }

    public static Optional<Direction> getByValue(int value) {
        return Optional.of(lookup.get(value));
    }

    public CoordDelta asCoordDelta() {
        return switch (this) {
            case up -> new CoordDelta(0, -1);
            case right -> new CoordDelta(1, 0);
            case down -> new CoordDelta(0, 1);
            case left -> new CoordDelta(-1, 0);
        };
    }
}
