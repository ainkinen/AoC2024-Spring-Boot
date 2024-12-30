package com.advent.aoc2024.days;

import static java.lang.Math.abs;

import com.advent.aoc2024.interfaces.BothParts;
import com.advent.aoc2024.utils.Bfs;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.Direction;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day20 implements BothParts {

    private final Bfs bfs;

    @Autowired
    public Day20(Bfs bfs) {
        this.bfs = bfs;
    }

    private List<Coord> getPath(String input) {
        Optional<Coord> maybeStart = Optional.empty();
        Optional<Coord> maybeEnd = Optional.empty();
        Set<Coord> nodes = new HashSet<>();

        String[] lines = input.split("\n");

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                Coord coord = new Coord(x, y);
                char c = line.charAt(x);
                switch (c) {
                    case 'S':
                        {
                            maybeStart = Optional.of(coord);
                            nodes.add(coord);
                            break;
                        }
                    case 'E':
                        {
                            maybeEnd = Optional.of(coord);
                            nodes.add(coord);
                            break;
                        }
                    case '.':
                        nodes.add(coord);
                }
            }
        }

        Coord start = maybeStart.orElseThrow();
        Coord end = maybeEnd.orElseThrow();

        return this.bfs
                .getShortestPath(
                        start,
                        c ->
                                Arrays.stream(Direction.values())
                                        .map(Direction::asCoordDelta)
                                        .map(c::plus)
                                        .filter(nodes::contains)
                                        .iterator(),
                        c -> c.equals(end))
                .orElseThrow();
    }

    private int dist(Coord a, Coord b) {
        return abs(b.x() - a.x()) + abs(b.y() - a.y());
    }

    public int solver(String input, int minStepsSaved, int maxShortcutLength) {
        List<Coord> path = getPath(input);

        if (path.isEmpty() || minStepsSaved > (path.size() - 1)) {
            throw new RuntimeException("Impossible to save this many steps");
        }

        int shortcuts = 0;

        int minJumpSize = minStepsSaved + 2;

        List<Coord> searchSpace = path.stream().limit(path.size() - minJumpSize).toList();

        for (int i = 0; i < searchSpace.size(); i++) {
            Coord start = path.get(i);

            IntStream possibleShortcutRange = IntStream.range(i + minJumpSize, path.size());
            Stream<Pair<Integer, Integer>> shortcutDestinations =
                    possibleShortcutRange
                            .mapToObj(toIdx -> Pair.of(toIdx, dist(start, path.get(toIdx))))
                            .filter(p -> p.getRight() <= maxShortcutLength);

            for (Pair<Integer, Integer> p : shortcutDestinations.toList()) {
                int stepsSaved = p.getLeft() - i - p.getRight();
                if (stepsSaved >= minStepsSaved) {
                    shortcuts++;
                }
            }
        }

        return shortcuts;
    }

    @Override
    public Integer part1(String input) {
        return solver(input, 100, 2);
    }

    @Override
    public Object part2(String input) {
        return solver(input, 100, 20);
    }
}
