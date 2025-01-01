package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.Direction;
import com.advent.aoc2024.utils.Location;

import org.apache.commons.lang3.IntegerRange;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Day06 implements Day {

    private record Input(
            Set<Coord> blocks, Location start, IntegerRange y_range, IntegerRange x_range) {}

    private Input parse(String input) {
        String[] lines = input.split("\n");
        int y_len = lines.length;
        int x_len = lines[0].length();

        Set<Coord> blocks = new HashSet<>();
        Location start = null;

        for (int y = 0; y < y_len; y++) {
            String line = lines[y];
            for (int x = 0; x < x_len; x++) {
                char c = line.charAt(x);

                switch (c) {
                    case '#':
                        blocks.add(new Coord(x, y));
                        continue;
                    case '^':
                        start = new Location(x, y, Direction.up);
                }
            }
        }

        return new Input(
                blocks, start, IntegerRange.of(0, x_len - 1), IntegerRange.of(0, y_len - 1));
    }

    private record SolverResult(Set<Location> visited, boolean is_loop) {}

    private SolverResult solver(
            Set<Coord> blocks, Location start, IntegerRange x_range, IntegerRange y_range) {
        Set<Location> visited = new HashSet<>();

        Location at = start;

        // Instead of stepping one by one, the code could move directly
        // to the next turn or edge.
        while (true) {
            if (visited.contains(at)) {
                return new SolverResult(visited, true);
            } else if (!at.inRange(x_range, y_range)) {
                return new SolverResult(visited, false);
            }

            visited.add(at);

            Location next = at.step();

            if (blocks.contains(next.getCoord())) {
                at = at.turnBy(90);
            } else {
                at = next;
            }
        }
    }

    @Override
    public Integer part1(String input) {
        Input parsedInput = parse(input);

        SolverResult solverResult =
                solver(
                        parsedInput.blocks,
                        parsedInput.start,
                        parsedInput.x_range,
                        parsedInput.y_range);

        return solverResult.visited.stream()
                .map(Location::getCoord)
                .collect(Collectors.toSet())
                .size();
    }

    @Override
    public Long part2(String input) {
        Input parsedInput = parse(input);

        SolverResult solverResult =
                solver(
                        parsedInput.blocks,
                        parsedInput.start,
                        parsedInput.x_range,
                        parsedInput.y_range);

        Set<Coord> original_path =
                solverResult.visited.stream().map(Location::getCoord).collect(Collectors.toSet());

        return original_path.stream()
                .filter(
                        c -> {
                            Set<Coord> edited_blocks = new HashSet<>(parsedInput.blocks);
                            edited_blocks.add(c);

                            SolverResult solver =
                                    solver(
                                            edited_blocks,
                                            parsedInput.start,
                                            parsedInput.x_range,
                                            parsedInput.y_range);

                            return solver.is_loop;
                        })
                .count();
    }
}
