package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Day04 implements Day {

    private final List<Pair<Integer, Integer>> deltas =
            List.of(
                    Pair.of(0, 1), // →
                    Pair.of(1, 1), // ↘
                    Pair.of(1, 0), // ↓
                    Pair.of(1, -1), // ↙
                    Pair.of(0, -1), // ←
                    Pair.of(-1, -1), // ↖
                    Pair.of(-1, 0), // ↑
                    Pair.of(-1, 1) // ↗
                    );

    ArrayList<char[]> parse(String input) {
        ArrayList<char[]> output = new ArrayList<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            output.add(line.toCharArray());
        }

        return output;
    }

    private boolean isMas(
            ArrayList<char[]> map, Pair<Integer, Integer> start, Pair<Integer, Integer> step) {
        int y_len = map.size();
        int x_len = map.getFirst().length;

        Range<Integer> y_range = Range.of(0, y_len - 1);
        Range<Integer> x_range = Range.of(0, x_len - 1);
        int max_y = start.getLeft() + step.getLeft() * 3;
        int max_x = start.getRight() + step.getRight() * 3;

        if (!y_range.contains(max_y) || !x_range.contains(max_x)) {
            return false;
        }

        int y = start.getLeft();
        int x = start.getRight();

        y += step.getLeft();
        x += step.getRight();
        if (map.get(y)[x] != 'M') return false;

        y += step.getLeft();
        x += step.getRight();
        if (map.get(y)[x] != 'A') return false;

        y += step.getLeft();
        x += step.getRight();
        return map.get(y)[x] == 'S';
    }

    private boolean isXmas(ArrayList<char[]> map, Pair<Integer, Integer> start) {
        int y_len = map.size();
        int x_len = map.getFirst().length;

        int y = start.getLeft();
        int x = start.getRight();

        if (y == 0 || x == 0 || y == y_len - 1 || x == x_len - 1) {
            // Edges. Can't have valid corners around this coord.
            return false;
        }

        String corners = "";
        corners += map.get(y - 1)[x - 1];
        corners += map.get(y - 1)[x + 1];
        corners += map.get(y + 1)[x + 1];
        corners += map.get(y + 1)[x - 1];

        List<String> rotations = List.of("MMSS", "SMMS", "SSMM", "MSSM");

        return rotations.contains(corners);
    }

    @Override
    public Object part1(String input) {
        ArrayList<char[]> map = parse(input);

        int xmasCount = 0;

        for (int y = 0; y < map.size(); y++) {
            char[] line = map.get(y);

            for (int x = 0; x < line.length; x++) {
                // Check only X coords as starting points
                if (map.get(y)[x] == 'X') {
                    // Any direction
                    for (Pair<Integer, Integer> delta : deltas) {

                        //noinspection SuspiciousNameCombination
                        if (isMas(map, Pair.of(y, x), delta)) {
                            xmasCount++;
                        }
                    }
                }
            }
        }

        return xmasCount;
    }

    @Override
    public Object part2(String input) {
        ArrayList<char[]> map = parse(input);

        int xmasCount = 0;

        for (int y = 0; y < map.size(); y++) {
            char[] line = map.get(y);

            for (int x = 0; x < line.length; x++) {
                // Check only A coords as starting points
                if (map.get(y)[x] == 'A') {
                    //noinspection SuspiciousNameCombination
                    if (isXmas(map, Pair.of(y, x))) {
                        xmasCount++;
                    }
                }
            }
        }

        return xmasCount;
    }
}
