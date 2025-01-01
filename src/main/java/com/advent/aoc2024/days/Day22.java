package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day22 implements Day {
    public static class SecretNumberGenerator implements Iterator<BigInteger> {
        BigInteger current;

        public SecretNumberGenerator(BigInteger current) {
            this.current = current;
        }

        public static BigInteger nextNumber(BigInteger number) {
            BigInteger n = number;
            BigInteger m = BigInteger.valueOf(16777216);

            n = n.shiftLeft(6).xor(n); // *64 and mix
            n = n.mod(m); // prune

            n = n.shiftRight(5).xor(n); // /32 and mix
            n = n.mod(m); // prune

            n = n.shiftLeft(11).xor(n); // *2048 and mix
            n = n.mod(m); // prune

            return n;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public BigInteger next() {
            BigInteger next = nextNumber(current);
            current = next;
            return next;
        }
    }

    private List<Integer> parse(String input) {
        String[] lines = input.split("\n");
        return Arrays.stream(lines).map(Integer::parseInt).toList();
    }

    @Override
    public Long part1(String input) {
        List<Integer> numbers = parse(input);
        List<BigInteger> nthNumbers =
                numbers.stream()
                        .map(
                                n -> {
                                    SecretNumberGenerator secretNumberGenerator =
                                            new SecretNumberGenerator(BigInteger.valueOf(n));
                                    return Stream.generate(secretNumberGenerator::next)
                                            .skip(1999)
                                            .findFirst()
                                            .orElseThrow();
                                })
                        .toList();

        return nthNumbers.stream().mapToLong(BigInteger::longValueExact).sum();
    }

    @Override
    public Long part2(String input) {
        List<Integer> numbers = parse(input);
        Map<List<Long>, Long> counter = new HashMap<>();

        for (long number : numbers) {
            SecretNumberGenerator gen = new SecretNumberGenerator(BigInteger.valueOf(number));

            Set<List<Long>> seen = new HashSet<>();

            List<Long> lastDigits =
                    Stream.generate(gen::next)
                            .limit(2000)
                            .map(bi -> bi.mod(BigInteger.valueOf(10)).longValueExact())
                            .toList();

            List<Pair<Long, Long>> priceAndChange =
                    IntStream.range(1, lastDigits.size())
                            .mapToObj(
                                    idx ->
                                            Pair.of(
                                                    lastDigits.get(idx),
                                                    lastDigits.get(idx) - lastDigits.get(idx - 1)))
                            .toList();

            // Windows of four
            for (int i = 3; i < priceAndChange.size(); i++) {
                long c1 = priceAndChange.get(i - 3).getRight();
                long c2 = priceAndChange.get(i - 2).getRight();
                long c3 = priceAndChange.get(i - 1).getRight();
                long c4 = priceAndChange.get(i).getRight();
                long v4 = priceAndChange.get(i).getLeft();

                List<Long> key = List.of(c1, c2, c3, c4);
                boolean notSeenYet = seen.add(key);
                if (notSeenYet) {
                    counter.merge(key, v4, Long::sum);
                }
            }
        }

        return counter.values().stream().max(Long::compareTo).orElseThrow();
    }
}
