package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.BothParts;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.CoordDelta;

import org.apache.commons.lang3.IntegerRange;
import org.apache.commons.math3.util.Combinations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day08 implements BothParts {

    private record Input(Map<Character, Set<Coord>> antennas, IntegerRange range) {}

    private Input parseInput(String input) {
        HashMap<Character, Set<Coord>> antennas = new HashMap<>();

        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c != '.') {
                    antennas.computeIfAbsent(c, k -> new HashSet<>()).add(new Coord(x, y));
                }
            }
        }

        return new Input(antennas, IntegerRange.of(0, lines.length - 1));
    }

    private Set<Coord> getAntiNodes(Set<Coord> antennas, IntegerRange range) {
        List<Coord> antenna_list = new ArrayList<>(antennas);
        Set<Coord> antiNodes = new HashSet<>();

        Combinations antenna_pairs = new Combinations(antenna_list.size(), 2);
        antenna_pairs.forEach(
                p -> {
                    Coord a = antenna_list.get(p[0]);
                    Coord b = antenna_list.get(p[1]);

                    CoordDelta delta = a.deltaTo(b);

                    Coord antiA = a.minus(delta);
                    if (antiA.inRange(range)) {
                        antiNodes.add(antiA);
                    }
                    Coord antiB = b.plus(delta);
                    if (antiB.inRange(range)) {
                        antiNodes.add(antiB);
                    }
                });

        return antiNodes;
    }

    private Set<Coord> getHarmonicAntiNodes(Set<Coord> antennas, IntegerRange range) {
        List<Coord> antenna_list = new ArrayList<>(antennas);
        Set<Coord> antiNodes = new HashSet<>();

        Combinations antenna_pairs = new Combinations(antenna_list.size(), 2);
        antenna_pairs.forEach(
                p -> {
                    Coord a = antenna_list.get(p[0]);
                    Coord b = antenna_list.get(p[1]);

                    CoordDelta delta = a.deltaTo(b);

                    Coord antiA = a;
                    while (antiA.inRange(range)) {
                        antiNodes.add(antiA);
                        antiA = antiA.minus(delta);
                    }

                    Coord antiB = b;
                    while (antiB.inRange(range)) {
                        antiNodes.add(antiB);
                        antiB = antiB.plus(delta);
                    }
                });

        return antiNodes;
    }

    @Override
    public Integer part1(String input) {
        Input parsed = parseInput(input);

        Set<Coord> antiNodes =
                parsed.antennas.values().stream()
                        .flatMap(s -> getAntiNodes(s, parsed.range).stream())
                        .collect(Collectors.toSet());

        return antiNodes.size();
    }

    @Override
    public Object part2(String input) {
        Input parsed = parseInput(input);

        Set<Coord> antiNodes =
                parsed.antennas.values().stream()
                        .flatMap(s -> getHarmonicAntiNodes(s, parsed.range).stream())
                        .collect(Collectors.toSet());

        // graphNodes(antiNodes, parsed.antennas, parsed.range);

        return antiNodes.size();
    }

    @SuppressWarnings("unused")
    private void graphNodes(
            Set<Coord> antiNodes, Map<Character, Set<Coord>> antennas, IntegerRange range) {
        for (int y = 0; y <= range.getMaximum(); y++) {
            for (int x = 0; x <= range.getMaximum(); x++) {
                Coord at = new Coord(x, y);
                char c = '.';
                if (antiNodes.contains(at)) {
                    c = '#';
                }

                Optional<Map.Entry<Character, Set<Coord>>> match =
                        antennas.entrySet().stream()
                                .filter(e -> e.getValue().contains(at))
                                .findFirst();
                if (match.isPresent()) {
                    c = match.get().getKey();
                }
                System.out.print(c);
            }

            System.out.println();
        }
    }
}
