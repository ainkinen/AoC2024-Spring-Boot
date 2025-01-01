package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;
import com.advent.aoc2024.utils.Bfs;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.Direction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
public class Day10 implements Day {

    private final Bfs bfs;

    @Autowired
    public Day10(Bfs bfs) {
        this.bfs = bfs;
    }

    private HashMap<Coord, Integer> parseMap(String input) {
        HashMap<Coord, Integer> map = new HashMap<>();

        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                map.put(new Coord(x, y), Character.getNumericValue(c));
            }
        }

        return map;
    }

    private Integer trailHeadScore(Coord start, HashMap<Coord, Integer> map) {
        return bfs.getAllShortestPaths(
                        start,
                        at -> {
                            int currentValue = map.get(at);
                            return Arrays.stream(Direction.values())
                                    .map(Direction::asCoordDelta)
                                    .map(at::plus)
                                    .filter(map::containsKey) // Still on map
                                    .filter(e -> map.get(e) == currentValue + 1) // One step higher
                                    .iterator();
                        },
                        c -> map.get(c) == 9)
                .size();
    }

    private Integer trailHeadRating(Coord start, HashMap<Coord, Integer> map) {
        return bfs.getAllUniquePaths(
                        start,
                        at -> {
                            int currentValue = map.get(at);
                            return Arrays.stream(Direction.values())
                                    .map(Direction::asCoordDelta)
                                    .map(at::plus)
                                    .filter(map::containsKey) // Still on map
                                    .filter(e -> map.get(e) == currentValue + 1) // One step higher
                                    .iterator();
                        },
                        c -> map.get(c) == 9)
                .size();
    }

    @Override
    public Integer part1(String input) {
        HashMap<Coord, Integer> map = parseMap(input);

        return map.entrySet().stream()
                .filter(e -> e.getValue() == 0)
                .mapToInt(e -> trailHeadScore(e.getKey(), map))
                .sum();
    }

    @Override
    public Integer part2(String input) {
        HashMap<Coord, Integer> map = parseMap(input);

        return map.entrySet().stream()
                .filter(e -> e.getValue() == 0)
                .mapToInt(e -> trailHeadRating(e.getKey(), map))
                .sum();
    }
}
