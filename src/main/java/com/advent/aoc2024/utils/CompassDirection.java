package com.advent.aoc2024.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum CompassDirection {
    N(0),
    NE(45),
    E(90),
    SE(135),
    S(180),
    SW(225),
    W(270),
    NW(315);

    private static final Map<Integer, CompassDirection> lookup = new HashMap<>();

    static {
        for (CompassDirection direction : CompassDirection.values()) {
            lookup.put(direction.value, direction);
        }
    }

    public final int value;

    CompassDirection(int value) {
        this.value = value;
    }

    public static Optional<CompassDirection> getByValue(int value) {
        return Optional.of(lookup.get(value));
    }

    public CoordDelta asCoordDelta() {
        return switch (this) {
            case N -> new CoordDelta(0, -1);
            case NE -> new CoordDelta(1, -1);
            case E -> new CoordDelta(1, 0);
            case SE -> new CoordDelta(1, 1);
            case S -> new CoordDelta(0, 1);
            case SW -> new CoordDelta(-1, 1);
            case W -> new CoordDelta(-1, 0);
            case NW -> new CoordDelta(-1, -1);
        };
    }
}
