package com.advent.aoc2024.days;

import static java.lang.Math.abs;

import com.advent.aoc2024.interfaces.Day;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Component
public class Day02 implements Day {

    private List<List<Integer>> parse(String input) {
        return input.lines()
                .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).toList())
                .toList();
    }

    private boolean allIncreasing(List<Integer> l) {
        return IntStream.range(1, l.size())
                .mapToObj(i -> Pair.of(l.get(i - 1), l.get(i)))
                .allMatch(pair -> pair.getLeft() < pair.getRight());
    }

    private boolean allDecreasing(List<Integer> l) {
        return IntStream.range(1, l.size())
                .mapToObj(i -> Pair.of(l.get(i - 1), l.get(i)))
                .allMatch(pair -> pair.getLeft() > pair.getRight());
    }

    private boolean maxDifferBy(List<Integer> l, int maxDiff) {
        return IntStream.range(1, l.size())
                .mapToObj(i -> Pair.of(l.get(i - 1), l.get(i)))
                .allMatch(pair -> abs(pair.getLeft() - pair.getRight()) <= maxDiff);
    }

    @Override
    public Long part1(String input) {
        List<List<Integer>> sequences = parse(input);

        Predicate<List<Integer>> increasingOrDecreasing = l -> allIncreasing(l) || allDecreasing(l);
        Predicate<List<Integer>> maxDifferBy3 = l -> maxDifferBy(l, 3);

        return sequences.stream().filter(increasingOrDecreasing).filter(maxDifferBy3).count();
    }

    @Override
    public Long part2(String input) {
        List<List<Integer>> sequences = parse(input);

        Predicate<List<Integer>> isSafe =
                l -> (allIncreasing(l) || allDecreasing(l)) && maxDifferBy(l, 3);

        return sequences.stream()
                .filter(
                        sequence -> {
                            if (isSafe.test(sequence)) {
                                return true;
                            }
                            for (int i = 0; i < sequence.size(); i++) {
                                List<Integer> newList = new ArrayList<>(sequence);
                                newList.remove(i);
                                if (isSafe.test(newList)) {
                                    return true;
                                }
                            }
                            return false;
                        })
                .count();
    }
}
