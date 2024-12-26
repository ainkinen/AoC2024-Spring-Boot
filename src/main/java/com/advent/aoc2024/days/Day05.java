package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.BothParts;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Day05 implements BothParts {

    private List<Pair<Integer, Integer>> parseRules(String input) {
        String rules_part = input.split("\n\n")[0];

        List<Pair<Integer, Integer>> pairs = new ArrayList<>();

        for (String s : rules_part.split("\n")) {
            String[] numbers = s.split("\\|");
            pairs.add(Pair.of(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])));
        }

        return pairs;
    }

    private List<List<Integer>> parseUpdates(String input) {
        String updates_part = input.split("\n\n")[1];

        List<List<Integer>> pairs = new ArrayList<>();

        for (String s : updates_part.split("\n")) {
            String[] numbers = s.split(",");
            pairs.add(Arrays.stream(numbers).map(Integer::parseInt).toList());
        }

        return pairs;
    }

    private boolean updateIsCorrect(List<Integer> update, List<Pair<Integer, Integer>> rules) {
        HashMap<Integer, Integer> page_to_idx = new HashMap<>();
        for (int idx = 0; idx < update.size(); idx++) {
            page_to_idx.put(update.get(idx), idx);
        }

        return rules.stream()
                .allMatch(
                        rule -> {
                            int l = rule.getLeft();
                            int r = rule.getRight();
                            if (!page_to_idx.containsKey(l) || !page_to_idx.containsKey(r)) {
                                return true;
                            }

                            return page_to_idx.get(l) < page_to_idx.get(r);
                        });
    }

    @Override
    public Long part1(String input) {
        List<Pair<Integer, Integer>> rules = parseRules(input);
        List<List<Integer>> updates = parseUpdates(input);

        List<List<Integer>> correctUpdates =
                updates.stream().filter(u -> updateIsCorrect(u, rules)).toList();

        return correctUpdates.stream().mapToLong(l -> l.get(l.size() / 2)).sum();
    }

    private List<Integer> fixUpdate(List<Integer> update, List<Pair<Integer, Integer>> rules) {
        ArrayList<Integer> fixed = new ArrayList<>(update);

        outer:
        while (true) {
            for (int i = 0; i < fixed.size() - 1; i++) {
                int l = fixed.get(i);
                int r = fixed.get(i + 1);
                if (rules.contains(Pair.of(r, l))) {
                    Collections.swap(fixed, i, i + 1);
                    continue outer;
                }
            }
            break;
        }

        return fixed;
    }

    @Override
    public Long part2(String input) {
        List<Pair<Integer, Integer>> rules = parseRules(input);
        List<List<Integer>> updates = parseUpdates(input);

        List<List<Integer>> incorrectUpdates =
                updates.stream().filter(u -> !updateIsCorrect(u, rules)).toList();

        return incorrectUpdates.stream()
                .map(u -> fixUpdate(u, rules))
                .mapToLong(l -> l.get(l.size() / 2))
                .sum();
    }
}
