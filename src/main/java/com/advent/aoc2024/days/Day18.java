package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;
import com.advent.aoc2024.utils.Bfs;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.Direction;

import org.apache.commons.lang3.IntegerRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day18 implements Day {

    Bfs bfs;

    @Autowired
    public Day18(Bfs bfs) {
        this.bfs = bfs;
    }

    Pattern coordPattern = Pattern.compile("(\\d+),(\\d+)");

    private List<Coord> parse(String input) {
        return coordPattern
                .matcher(input)
                .results()
                .map(
                        r -> {
                            int x = Integer.parseInt(r.group(1));
                            int y = Integer.parseInt(r.group(2));
                            return new Coord(x, y);
                        })
                .toList();
    }

    @SuppressWarnings("unused")
    private void graph(Collection<Coord> path, Collection<Coord> corruptions, int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Coord at = new Coord(x, y);
                if (path.contains(at)) {
                    System.out.print('O');
                } else if (corruptions.contains(at)) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }

    private Optional<List<Coord>> getShortestPath(Collection<Coord> corruptions, int size) {
        return this.bfs.getShortestPath(
                new Coord(0, 0),
                coord ->
                        Arrays.stream(Direction.values())
                                .map(Direction::asCoordDelta)
                                .map(coord::plus)
                                .filter(c -> c.inRange(IntegerRange.of(0, size - 1)))
                                .filter(c -> !corruptions.contains(c))
                                .iterator(),
                at -> at.x() == size - 1 && at.y() == size - 1);
    }

    public int solverPart1(String input, int steps, int size) {
        List<Coord> allCorruptions = parse(input);

        Set<Coord> firstCorruptions =
                allCorruptions.stream().limit(steps).collect(Collectors.toSet());

        Optional<List<Coord>> shortestPath = getShortestPath(firstCorruptions, size);

        // graph(shortestPath.orElseThrow(), firstCorruptions, size);

        return shortestPath.orElseThrow().size() - 1; // Ignore starting point
    }

    public static <T> int partitionPoint(List<T> list, Predicate<T> predicate) {
        int left = 0;
        int right = list.size();

        while (left < right) {
            int mid = (left + right) / 2;
            if (predicate.test(list.get(mid))) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public String part2Solver(String input, int size) {
        List<Coord> corruptions = parse(input);
        List<Integer> dropCounts = IntStream.range(0, corruptions.size()).boxed().toList();

        Predicate<Integer> pathStillOpen =
                c -> {
                    Set<Coord> set = corruptions.stream().limit(c).collect(Collectors.toSet());
                    return getShortestPath(set, size).isPresent();
                };

        int partition_point = partitionPoint(dropCounts, pathStillOpen);

        Coord firstBlocker = corruptions.get(partition_point - 1);

        return firstBlocker.x() + "," + firstBlocker.y();
    }

    @Override
    public Integer part1(String input) {
        return solverPart1(input, 1024, 71);
    }

    @Override
    public String part2(String input) {
        return part2Solver(input, 71);
    }
}
