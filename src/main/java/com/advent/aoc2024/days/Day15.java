package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.BothParts;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.CoordDelta;
import com.advent.aoc2024.utils.Direction;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day15 implements BothParts {

    private record Input(
            Map<Coord, Character> map,
            List<Direction> moves,
            Coord robotAt,
            int len_x,
            int len_y) {}

    private Input parseInput(String input) {
        String[] parts = input.split("\n\n");
        String mapPart = parts[0];
        String movesPart = parts[1].trim();

        Map<Coord, Character> map = new HashMap<>();
        Optional<Coord> robot_at = Optional.empty();

        String[] lines = mapPart.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                Coord coord = new Coord(x, y);
                switch (c) {
                    case '#':
                    case 'O':
                        {
                            map.put(coord, c);
                            continue;
                        }
                    case '@':
                        {
                            robot_at = Optional.of(coord);
                        }
                }
            }
        }

        List<Direction> moves = new ArrayList<>();
        for (char c : movesPart.toCharArray()) {
            Optional<Direction> maybeDir =
                    switch (c) {
                        case '^' -> Optional.of(Direction.up);
                        case '>' -> Optional.of(Direction.right);
                        case '<' -> Optional.of(Direction.left);
                        case 'v' -> Optional.of(Direction.down);
                        case '\n' -> Optional.empty();
                        default -> throw new RuntimeException("Unknown direction: " + c);
                    };
            maybeDir.ifPresent(moves::add);
        }

        return new Input(map, moves, robot_at.orElseThrow(), lines.length, lines[0].length());
    }

    private Map<Coord, Character> doubleMap(Map<Coord, Character> map) {

        return map.entrySet().stream()
                .flatMap(
                        e -> {
                            Coord orig = e.getKey();
                            Coord left = new Coord(orig.x() * 2, orig.y());
                            Coord right = new Coord((orig.x() * 2) + 1, orig.y());
                            return switch (e.getValue()) {
                                case '#' -> Stream.of(Pair.of(left, '#'), Pair.of(right, '#'));
                                case 'O' -> Stream.of(Pair.of(left, '['), Pair.of(right, ']'));
                                default -> throw new RuntimeException("Unknown map character");
                            };
                        })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Input parseDoubleInput(String inputStr) {
        Input parsed = parseInput(inputStr);

        return new Input(
                doubleMap(parsed.map),
                parsed.moves,
                new Coord(parsed.robotAt.x() * 2, parsed.robotAt.y()),
                parsed.len_x * 2,
                parsed.len_y);
    }

    private Optional<List<Coord>> chainToMove(
            Map<Coord, Character> map, Coord robotAt, Direction dir) {
        CoordDelta step = dir.asCoordDelta();
        List<Coord> chain = new ArrayList<>();

        Coord head = robotAt.plus(step);
        while (map.containsKey(head)) {
            char c = map.get(head);

            if (c == '#') {
                return Optional.empty();
            }
            chain.add(head);
            head = head.plus(step);
        }
        return Optional.of(chain);
    }

    private Coord stepSimple(Map<Coord, Character> map, Coord robotAt, Direction dir) {
        CoordDelta step = dir.asCoordDelta();

        Optional<List<Coord>> maybeChain = chainToMove(map, robotAt, dir);

        if (maybeChain.isEmpty()) {
            // Can't move
            return robotAt;
        }

        List<Coord> chain = maybeChain.get();

        // Move the chain
        Coord inFrontOfRobot = robotAt.plus(step);
        if (!chain.isEmpty()) {
            // The movable chain is valid only if there is an empty spot in front.
            // This makes it safe to swap into it.
            Coord inFront = chain.getLast().plus(step);
            for (Coord box : chain.reversed()) {
                map.put(inFront, map.get(box));
                inFront = box;
            }
            map.remove(inFront); // Remove last box
        }

        return inFrontOfRobot;
    }

    private Optional<Set<Coord>> itemsToMove(
            Map<Coord, Character> map, Coord robotAt, Direction dir) {
        CoordDelta step = dir.asCoordDelta();

        Set<Coord> coordsToMove = new HashSet<>();
        ArrayList<Coord> todo = new ArrayList<>(List.of(robotAt));

        while (!todo.isEmpty()) {
            Coord coord = todo.removeLast();
            Coord next = coord.plus(step);

            if (map.containsKey(next)) {
                Character tile = map.get(next);

                switch (tile) {
                    case '#':
                        {
                            // Hitting a wall. Can't move anything.
                            return Optional.empty();
                        }
                    case '[':
                        {
                            for (Coord boxSide : List.of(next, new Coord(next.x() + 1, next.y()))) {
                                if (coordsToMove.add(boxSide)) {
                                    todo.add(boxSide);
                                }
                            }
                            break;
                        }
                    case ']':
                        {
                            for (Coord boxSide : List.of(next, new Coord(next.x() - 1, next.y()))) {
                                if (coordsToMove.add(boxSide)) {
                                    todo.add(boxSide);
                                }
                            }
                            break;
                        }
                    default:
                        {
                            throw new RuntimeException("Unreachable");
                        }
                }
            }
        }

        return Optional.of(coordsToMove);
    }

    private Coord stepComplicated(Map<Coord, Character> map, Coord robotAt, Direction dir) {
        CoordDelta step = dir.asCoordDelta();

        Optional<Set<Coord>> maybeItemsToMove = itemsToMove(map, robotAt, dir);

        if (maybeItemsToMove.isEmpty()) {
            // Can't move
            return robotAt;
        }

        List<Coord> toMove = maybeItemsToMove.get().stream().toList();

        Coord newRobot = robotAt.plus(step);

        switch (dir) {
            case Direction.up:
                {
                    // Sort up-to-down
                    toMove =
                            toMove.stream().toList().stream()
                                    .sorted(Comparator.comparing(Coord::y))
                                    .toList();
                    break;
                }
            case Direction.down:
                {
                    // Sort down-to-up
                    toMove =
                            toMove.stream().toList().stream()
                                    .sorted(Comparator.comparing(Coord::y))
                                    .toList()
                                    .reversed();
                    break;
                }
            default:
                {
                    throw new RuntimeException("Should not be used with left-right");
                }
        }

        // Move boxes. No overwrites because of sorting.
        for (Coord coord : toMove) {
            char c = map.remove(coord);
            map.put(coord.plus(step), c);
        }

        return newRobot;
    }

    @SuppressWarnings("unused")
    private void graphMap(Input input, Map<Coord, Character> map, Coord robotAt) {
        for (int y = 0; y < input.len_y; y++) {
            for (int x = 0; x < input.len_x; x++) {
                Coord coord = new Coord(x, y);
                if (coord.equals(robotAt)) {
                    System.out.print("@");
                } else if (map.containsKey(coord)) {
                    System.out.print(map.get(coord));
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    @Override
    public Integer part1(String inputStr) {
        Input input = parseInput(inputStr);
        Map<Coord, Character> map = input.map;
        Coord robotAt = input.robotAt;

        // System.out.println("Initial state");
        // graphMap(input, input.map, robotAt);

        for (Direction dir : input.moves) {
            robotAt = stepSimple(map, robotAt, dir);
            // System.out.println("Dir: " + dir);
            // graphMap(input, input.map, robotAt);
        }

        return map.entrySet().stream()
                .filter(e -> e.getValue() == 'O')
                .mapToInt(e -> e.getKey().y() * 100 + e.getKey().x())
                .sum();
    }

    @Override
    public Object part2(String inputStr) {
        Input input = parseDoubleInput(inputStr);
        Map<Coord, Character> map = input.map;
        Coord robotAt = input.robotAt;

        // System.out.println("Initial state");
        // graphMap(input, input.map, robotAt);

        for (Direction dir : input.moves) {
            switch (dir) {
                case Direction.left:
                case Direction.right:
                    {
                        robotAt = stepSimple(map, robotAt, dir);
                        break;
                    }
                case Direction.up:
                case Direction.down:
                    {
                        robotAt = stepComplicated(map, robotAt, dir);
                        break;
                    }
            }
            // System.out.println("Dir: " + dir);
            // graphMap(input, input.map, robotAt);
        }

        return map.entrySet().stream()
                .filter(e -> e.getValue() == '[')
                .mapToInt(e -> e.getKey().y() * 100 + e.getKey().x())
                .sum();
    }
}
