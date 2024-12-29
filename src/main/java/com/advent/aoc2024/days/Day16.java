package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.BothParts;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.Direction;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day16 implements BothParts {
    private record Input(Set<Coord> graph, Coord start, Coord end) {}

    private Input parseInput(String input) {
        Set<Coord> graph = new HashSet<>();

        Optional<Coord> start = Optional.empty();
        Optional<Coord> end = Optional.empty();

        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                Coord coord = new Coord(x, y);

                if (List.of('.', 'S', 'E').contains(c)) {
                    graph.add(coord);
                }

                if (c == 'S') {
                    start = Optional.of(coord);
                }
                if (c == 'E') {
                    end = Optional.of(coord);
                }
            }
        }

        return new Input(graph, start.orElseThrow(), end.orElseThrow());
    }

    private record Path(int score, List<Coord> path, Direction dir) implements Comparable<Path> {
        @SuppressWarnings("NullableProblems")
        @Override
        public int compareTo(Path o) {
            return Comparator.comparing(Path::score).compare(this, o);
        }
    }

    private List<Path> getPaths(Input input) {
        PriorityQueue<Path> paths = new PriorityQueue<>();
        paths.add(new Path(0, List.of(input.start), Direction.right));

        List<Path> completedPaths = new ArrayList<>();
        Map<Pair<Coord, Direction>, Integer> locScores = new HashMap<>();

        while (!paths.isEmpty()) {
            Path state = paths.remove();

            if (state.path.getLast().equals(input.end)) {
                completedPaths.add(state);
                continue;
            }

            Arrays.stream(Direction.values())
                    .filter(
                            dir ->
                                    input.graph.contains(
                                            state.path.getLast().plus(dir.asCoordDelta())))
                    .forEach(
                            dir -> {
                                Coord next = state.path.getLast().plus(dir.asCoordDelta());
                                ArrayList<Coord> nextPath = new ArrayList<>(state.path);
                                nextPath.add(next);
                                int nextScore =
                                        dir.equals(state.dir)
                                                ? state.score + 1
                                                : state.score + 1001;

                                if (nextScore
                                        <= locScores.getOrDefault(
                                                Pair.of(next, dir), Integer.MAX_VALUE)) {
                                    locScores.put(Pair.of(next, dir), nextScore);
                                    paths.add(new Path(nextScore, nextPath, dir));
                                }
                            });
        }

        return completedPaths;
    }

    @Override
    public Integer part1(String inputStr) {
        Input input = parseInput(inputStr);

        List<Path> paths = getPaths(input);

        return paths.stream().mapToInt(path -> path.score).min().orElseThrow();
    }

    @Override
    public Integer part2(String inputStr) {
        Input input = parseInput(inputStr);

        List<Path> paths = getPaths(input);

        int minPathLength = paths.stream().mapToInt(path -> path.score).min().orElseThrow();

        Set<Coord> stepsInMinPaths =
                paths.stream()
                        .filter(p -> p.score == minPathLength)
                        .flatMap(path -> path.path.stream())
                        .collect(Collectors.toSet());

        return stepsInMinPaths.size();
    }
}
