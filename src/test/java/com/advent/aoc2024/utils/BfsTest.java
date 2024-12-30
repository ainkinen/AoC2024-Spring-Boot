package com.advent.aoc2024.utils;

import static org.junit.jupiter.api.Assertions.*;

import static java.lang.Math.abs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

class BfsTest {

    private Bfs bfs;

    @BeforeEach
    void setUp() {
        bfs = new Bfs();
    }

    @Nested
    class GetShortestPath {

        @Test
        void whenStartAndGoalAreEqualThenReturnsOnlyStart() {
            Optional<List<Integer>> shortestPath =
                    bfs.getShortestPath(1, i -> List.of(i - 1, i + 1).iterator(), i -> i == 1);
            assertTrue(shortestPath.isPresent());
            assertEquals(shortestPath.get(), List.of(1));
        }

        @Test
        void whenStartAndGoalDifferThenReturnsPathIncludingEnds() {
            Optional<List<Integer>> shortestPath =
                    bfs.getShortestPath(1, i -> List.of(i - 1, i + 1).iterator(), i -> i == 5);
            assertTrue(shortestPath.isPresent());
            assertEquals(shortestPath.get(), List.of(1, 2, 3, 4, 5));
        }

        @Test
        void whenEndIsNotReachableThenReturnsEmpty() {
            Optional<List<Integer>> shortestPath =
                    bfs.getShortestPath(1, i -> Collections.emptyIterator(), i -> i == -5);
            assertTrue(shortestPath.isEmpty());
        }
    }

    @Nested
    class GetAllShortestPaths {

        @Test
        void whenNoPathsThenReturnsEmptyList() {
            List<List<Integer>> paths =
                    bfs.getAllShortestPaths(1, i -> Collections.emptyIterator(), i -> i == 10);
            assertTrue(paths.isEmpty());
        }

        @Test
        void returnsAllShortestPaths() {
            List<List<Integer>> paths =
                    bfs.getAllShortestPaths(
                            0, i -> List.of(i - 1, i + 1).iterator(), i -> abs(i) == 3);
            assertEquals(2, paths.size());
            assertTrue(paths.containsAll(List.of(List.of(0, 1, 2, 3), List.of(0, -1, -2, -3))));
        }
    }

    @Nested
    class GetAllUniquePaths {
        private HashMap<Coord, Integer> map;
        private List<List<Coord>> paths;

        @BeforeEach
        void setUp() {
            // 0 1 .
            // 1 2 3
            // . 3 4
            map = new HashMap<>();
            map.put(new Coord(0, 0), 0);
            map.put(new Coord(0, 1), 1);
            map.put(new Coord(1, 0), 1);
            map.put(new Coord(1, 1), 2);
            map.put(new Coord(1, 2), 3);
            map.put(new Coord(2, 1), 3);
            map.put(new Coord(2, 2), 4);

            // Two splits -> four routes from 0 to 4
            paths =
                    bfs.getAllUniquePaths(
                            new Coord(0, 0),
                            at -> {
                                int currentValue = map.get(at);
                                return Arrays.stream(Direction.values())
                                        .map(Direction::asCoordDelta)
                                        .map(at::plus)
                                        .filter(map::containsKey)
                                        .filter(c -> map.get(c) == currentValue + 1)
                                        .iterator();
                            },
                            c -> map.get(c) == 4);
        }

        @Test
        void returnsAllPaths() {
            assertEquals(4, paths.size());
        }
    }
}
