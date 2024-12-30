package com.advent.aoc2024.days;

import static com.advent.aoc2024.utils.StringUtils.stripPrefix;

import com.advent.aoc2024.interfaces.BothParts;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class Day19 implements BothParts {

    private record Input(List<String> towels, List<String> patterns) {}

    private Input parse(String input) {
        String[] sections = input.split("\n\n");

        List<String> towels = Arrays.stream(sections[0].split(", ")).toList();
        List<String> patterns = Arrays.stream(sections[1].split("\n")).toList();

        return new Input(towels, patterns);
    }

    private long countWays(
            String string, List<String> patterns, HashMap<String, Long> cache, int depth) {

        if (cache.containsKey(string)) {
            return cache.get(string);
        }

        long ways = 0;

        for (String pattern : patterns) {

            if (Objects.equals(string, pattern)) {
                ways += 1;
            }

            Optional<String> maybeRemainder = stripPrefix(string, pattern);
            if (maybeRemainder.isPresent()) {
                ways += countWays(maybeRemainder.get(), patterns, cache, depth + 1);
            }
        }

        cache.put(string, ways);
        return ways;
    }

    @Override
    public Long part1(String inputStr) {
        Input input = parse(inputStr);

        String towelOptions = String.join("|", input.towels);
        Pattern towelPattern = Pattern.compile("^(" + towelOptions + ")+$");

        return input.patterns.stream().filter(p -> towelPattern.matcher(p).matches()).count();
    }

    @Override
    public Long part2(String inputStr) {
        Input input = parse(inputStr);

        HashMap<String, Long> cache = new HashMap<>();

        return input.patterns.stream().mapToLong(s -> countWays(s, input.towels, cache, 0)).sum();
    }
}
