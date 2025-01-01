package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class Day25 implements Day {
    private record Input(List<int[]> locks, List<int[]> keys) {}

    private Input parse(String input) {
        ArrayList<int[]> locks = new ArrayList<>();
        ArrayList<int[]> keys = new ArrayList<>();

        for (String section : input.split("\n\n")) {
            String[] lines = section.split("\n");
            String firstLine = lines[0];

            int[] columns = new int[5];
            for (int lineIdx = 1; lineIdx < 6; lineIdx++) {
                String line = lines[lineIdx];
                for (int charIdx = 0; charIdx < 5; charIdx++) {
                    columns[charIdx] += line.charAt(charIdx) == '#' ? 1 : 0;
                }
            }

            if (firstLine.equals("#####")) {
                locks.add(columns);
            } else {
                keys.add(columns);
            }
        }

        return new Input(locks, keys);
    }

    @Override
    public Integer part1(String inputStr) {
        Input input = parse(inputStr);

        int total = 0;

        for (int[] lock : input.locks) {
            for (int[] key : input.keys) {
                boolean allColumnsFit =
                        IntStream.range(0, lock.length)
                                .allMatch(index -> lock[index] + key[index] <= 5);
                if (allColumnsFit) {
                    total++;
                }
            }
        }

        return total;
    }

    @Override
    public String part2(String input) {
        return "Year done!";
    }
}
