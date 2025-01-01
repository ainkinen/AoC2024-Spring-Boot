package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class Day11 implements Day {

    private record MemoInput(long stone, int times) {}

    private static final LoadingCache<MemoInput, Long> CACHE =
            CacheBuilder.newBuilder()
                    .build(
                            CacheLoader.from(
                                    memoInput ->
                                            Day11.countRecursive(
                                                    memoInput.stone, memoInput.times)));

    private static long memoizedCountRecursively(long stone, int times) {
        MemoInput memoInput = new MemoInput(stone, times);
        try {
            return CACHE.get(memoInput);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Long> parseStones(String input) {
        return Arrays.stream(input.split(" "))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private static Optional<Pair<Long, Long>> splitStone(Long stone) {
        String stoneStr = stone.toString();
        List<Integer> digits = Arrays.stream(stoneStr.split("")).map(Integer::parseInt).toList();

        int numDigits = digits.size();
        if (numDigits % 2 != 0) {
            // Not even, no split
            return Optional.empty();
        }

        String strLeft = stoneStr.substring(0, numDigits / 2);
        String strRight = stoneStr.substring(numDigits / 2);
        long left = Long.parseLong(strLeft);
        long right = Long.parseLong(strRight);

        return Optional.of(Pair.of(left, right));
    }

    private static long countRecursive(long stone, int times) {
        if (times == 0) {
            return 1;
        }

        if (stone == 0) {
            return memoizedCountRecursively(1, times - 1);
        }

        Optional<Pair<Long, Long>> splitStones = splitStone(stone);
        if (splitStones.isPresent()) {
            Pair<Long, Long> stones = splitStones.get();
            return memoizedCountRecursively(stones.getLeft(), times - 1)
                    + memoizedCountRecursively(stones.getRight(), times - 1);
        }

        return memoizedCountRecursively(stone * 2024, times - 1);
    }

    @Override
    public Long part1(String input) {
        List<Long> stones = parseStones(input);
        long total = stones.stream().mapToLong(s -> memoizedCountRecursively(s, 25)).sum();
        CACHE.invalidateAll();
        return total;
    }

    @Override
    public Long part2(String input) {
        List<Long> stones = parseStones(input);
        long total = stones.stream().mapToLong(s -> memoizedCountRecursively(s, 75)).sum();
        CACHE.invalidateAll();
        return total;
    }
}
