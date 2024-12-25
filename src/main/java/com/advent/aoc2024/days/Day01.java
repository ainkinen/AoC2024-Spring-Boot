package com.advent.aoc2024.days;

import static java.lang.Math.abs;

import com.advent.aoc2024.interfaces.BothParts;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Day01 implements BothParts {
    private record Lists(List<Integer> left, List<Integer> right) {}

    private Lists parse(String input) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        input.lines()
                .forEach(
                        line -> {
                            String[] parts = line.split("\\s+");
                            left.add(Integer.parseInt(parts[0]));
                            right.add(Integer.parseInt(parts[1]));
                        });

        return new Lists(left, right);
    }

    @Override
    public Object part1(String input) {
        Lists lists = parse(input);

        List<Integer> left = lists.left;
        left.sort(Integer::compareTo);
        List<Integer> right = lists.right;
        right.sort(Integer::compareTo);

        int total = 0;

        for (int i = 0; i < left.size(); i++) {
            total += abs(left.get(i) - right.get(i));
        }

        return total;
    }

    @Override
    public Object part2(String input) {
        Lists lists = parse(input);

        Map<Integer, Long> frequencies =
                lists.right.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long total = 0;

        for (int i : lists.left) {
            total += i * frequencies.getOrDefault(i, 0L);
        }

        return total;
    }
}
