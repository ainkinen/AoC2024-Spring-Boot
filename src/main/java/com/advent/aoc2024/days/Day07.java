package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class Day07 implements Day {

    static final Pattern number = Pattern.compile("(\\d+)");

    private record Equation(long result, List<Long> numbers) {}

    private List<Equation> parse(String input) {
        List<Equation> equations = new ArrayList<>();
        input.lines()
                .forEach(
                        line -> {
                            Iterator<Long> numbers_iter =
                                    number.matcher(line)
                                            .results()
                                            .map(r -> Long.parseLong(r.group(1)))
                                            .iterator();

                            long result = numbers_iter.next();

                            List<Long> numbers = new ArrayList<>();
                            numbers_iter.forEachRemaining(numbers::add);

                            equations.add(new Equation(result, numbers));
                        });

        return equations;
    }

    private Set<Long> allResults(List<Long> numbers) {
        List<Long> numbers_left = new ArrayList<>(numbers);

        Set<Long> results = new HashSet<>();
        results.add(numbers_left.removeFirst());

        while (!numbers_left.isEmpty()) {
            long next_number = numbers_left.removeFirst();

            Set<Long> new_results = new HashSet<>();
            for (long result : results) {
                new_results.add(result + next_number);
                new_results.add(result * next_number);
            }

            results = new_results;
        }

        return results;
    }

    private Set<Long> allResultsWithConcat(List<Long> numbers) {
        List<Long> numbers_left = new ArrayList<>(numbers);

        Set<Long> results = new HashSet<>();
        results.add(numbers_left.removeFirst());

        while (!numbers_left.isEmpty()) {
            long next_number = numbers_left.removeFirst();

            Set<Long> new_results = new HashSet<>();
            for (long result : results) {
                new_results.add(result + next_number);
                new_results.add(result * next_number);
                new_results.add(Long.parseLong(Long.toString(result) + next_number));
            }

            results = new_results;
        }

        return results;
    }

    @Override
    public Long part1(String input) {
        List<Equation> equations = parse(input);

        return equations.stream()
                .filter(
                        equation ->
                                allResults(equation.numbers).stream()
                                        .anyMatch(i -> i == equation.result))
                .mapToLong(Equation::result)
                .sum();
    }

    @Override
    public Long part2(String input) {
        List<Equation> equations = parse(input);

        return equations.stream()
                .filter(
                        equation ->
                                allResultsWithConcat(equation.numbers).stream()
                                        .anyMatch(i -> i == equation.result))
                .mapToLong(Equation::result)
                .sum();
    }
}
