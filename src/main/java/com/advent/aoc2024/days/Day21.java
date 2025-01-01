package com.advent.aoc2024.days;

import static java.lang.Math.abs;

import com.advent.aoc2024.interfaces.Day;
import com.advent.aoc2024.utils.*;

import org.apache.commons.collections4.iterators.PermutationIterator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day21 implements Day {
    private final HashMap<Character, Coord> NUMERICAL;
    private final HashMap<Character, Coord> DIRECTIONAL;
    private final HashMap<CoordDelta, Character> DIR_MAP;

    public Day21() {
        NUMERICAL = new HashMap<>();
        NUMERICAL.put('7', new Coord(0, 0));
        NUMERICAL.put('8', new Coord(1, 0));
        NUMERICAL.put('9', new Coord(2, 0));
        NUMERICAL.put('4', new Coord(0, 1));
        NUMERICAL.put('5', new Coord(1, 1));
        NUMERICAL.put('6', new Coord(2, 1));
        NUMERICAL.put('1', new Coord(0, 2));
        NUMERICAL.put('2', new Coord(1, 2));
        NUMERICAL.put('3', new Coord(2, 2));
        // NUMERICAL.put(' ', new Coord(0, 3));
        NUMERICAL.put('0', new Coord(1, 3));
        NUMERICAL.put('A', new Coord(2, 3));

        DIRECTIONAL = new HashMap<>();
        // DIRECTIONAL.put (' ', new Coord(0, 0));
        DIRECTIONAL.put('^', new Coord(1, 0));
        DIRECTIONAL.put('A', new Coord(2, 0));
        DIRECTIONAL.put('<', new Coord(0, 1));
        DIRECTIONAL.put('v', new Coord(1, 1));
        DIRECTIONAL.put('>', new Coord(2, 1));

        DIR_MAP = new HashMap<>();
        DIR_MAP.put(new CoordDelta(0, -1), '^');
        DIR_MAP.put(new CoordDelta(0, 1), 'v');
        DIR_MAP.put(new CoordDelta(-1, 0), '<');
        DIR_MAP.put(new CoordDelta(1, 0), '>');
    }

    private List<String> routesFromTo(HashMap<Character, Coord> keypad, Coord start, Coord end) {

        CoordDelta startToEnd = start.deltaTo(end);
        Direction horizontal =
                Integer.signum(startToEnd.x()) == -1 ? Direction.left : Direction.right;
        Direction vertical = Integer.signum(startToEnd.y()) == -1 ? Direction.up : Direction.down;
        List<Direction> pathDirections =
                Stream.concat(
                                Stream.generate(() -> horizontal).limit(abs(startToEnd.x())),
                                Stream.generate(() -> vertical).limit(abs(startToEnd.y())))
                        .toList();

        PermutationIterator<Direction> pathPermutations = new PermutationIterator<>(pathDirections);
        Set<List<Direction>> routes = new HashSet<>();
        pathPermutations.forEachRemaining(routes::add);

        Function<List<Direction>, List<Coord>> toCoords =
                (list) -> {
                    List<Coord> coords = new ArrayList<>();
                    Coord at = start;
                    coords.add(at);
                    for (Direction dir : list) {
                        Coord next = at.plus(dir.asCoordDelta());
                        coords.add(next);
                        at = next;
                    }
                    return coords;
                };

        Predicate<List<Coord>> validRoute = (list) -> list.stream().allMatch(keypad::containsValue);

        List<List<Coord>> filteredRoutes =
                routes.stream().map(toCoords).filter(validRoute).toList();

        return filteredRoutes.stream()
                .map(
                        p -> {
                            StringBuilder s = new StringBuilder();
                            Coord prev = p.getFirst();

                            for (int i = 1; i < p.size(); i++) {
                                Coord next = p.get(i);
                                CoordDelta delta =
                                        new CoordDelta(next.x() - prev.x(), next.y() - prev.y());
                                char c = DIR_MAP.get(delta);
                                s.append(c);
                                prev = next;
                            }
                            s.append('A');
                            return s.toString();
                        })
                .toList();
    }

    private long shortestPath(
            Character from, Character to, int level, int maxLevel, HashMap<String, Long> cache) {
        if (level == maxLevel - 1) {
            return 1;
        }

        String cache_key = "" + from + to + level + maxLevel;
        if (cache.containsKey(cache_key)) {
            return cache.get(cache_key);
        }

        List<String> routes =
                (level == 0)
                        ? routesFromTo(NUMERICAL, NUMERICAL.get(from), NUMERICAL.get(to))
                        : routesFromTo(DIRECTIONAL, DIRECTIONAL.get(from), DIRECTIONAL.get(to));

        long min =
                routes.stream()
                        .mapToLong(
                                r -> {
                                    long total = 0;
                                    char cur = 'A';
                                    for (char next : r.toCharArray()) {
                                        total +=
                                                shortestPath(cur, next, level + 1, maxLevel, cache);
                                        cur = next;
                                    }
                                    return total;
                                })
                        .min()
                        .orElseThrow();

        cache.put(cache_key, min);
        return min;
    }

    private long shortCode(String code, int levels) {
        HashMap<String, Long> cache = new HashMap<>();
        char from = 'A';
        long total = 0;
        for (char to : code.toCharArray()) {
            total += shortestPath(from, to, 0, levels, cache);
            from = to;
        }

        return total;
    }

    private Long solve(String input, int levels) {
        String[] codes = input.split("\n");

        List<Long> shortestDistances =
                Arrays.stream(codes).map(code -> shortCode(code, levels)).toList();

        List<Integer> numericalParts =
                Arrays.stream(codes)
                        .map(code -> code.substring(0, 3))
                        .map(Integer::parseInt)
                        .toList();

        return IntStream.range(0, codes.length)
                .mapToLong(i -> numericalParts.get(i) * shortestDistances.get(i))
                .sum();
    }

    @Override
    public Long part1(String input) {
        return solve(input, 4);
    }

    @Override
    public Long part2(String input) {
        return solve(input, 27);
    }
}
