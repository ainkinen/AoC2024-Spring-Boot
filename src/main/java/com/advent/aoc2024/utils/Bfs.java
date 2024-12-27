package com.advent.aoc2024.utils;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class Bfs {

    public <N> Optional<List<N>> getShortestPath(
            N start, Function<N, Iterator<N>> successors, Function<N, Boolean> success) {

        Set<N> visited = new HashSet<>();

        List<List<N>> paths = new ArrayList<>();
        paths.add(new ArrayList<>(Collections.singletonList(start)));

        while (!paths.isEmpty()) {
            List<List<N>> newPaths = new ArrayList<>();
            for (List<N> path : paths) {
                N head = path.getLast();

                // Check if end has been reached
                if (success.apply(head)) {
                    return Optional.of(path);
                }

                for (Iterator<N> it = successors.apply(head); it.hasNext(); ) {
                    N n = it.next();
                    if (visited.contains(n)) {
                        continue;
                    }
                    visited.add(n);
                    List<N> extendedPath = new ArrayList<>(path);
                    extendedPath.add(n);
                    newPaths.add(extendedPath);
                }
            }

            paths = newPaths;
        }

        return Optional.empty();
    }

    public <N> List<List<N>> getAllShortestPaths(
            N start, Function<N, Iterator<N>> successors, Function<N, Boolean> success) {
        List<List<N>> completedPaths = new ArrayList<>();

        Set<N> visited = new HashSet<>();

        List<List<N>> paths = new ArrayList<>();
        paths.add(new ArrayList<>(Collections.singletonList(start)));

        while (!paths.isEmpty()) {
            List<List<N>> newPaths = new ArrayList<>();
            for (List<N> path : paths) {
                N head = path.getLast();

                // Check if end has been reached
                if (success.apply(head)) {
                    completedPaths.add(path);
                    continue;
                }

                for (Iterator<N> it = successors.apply(head); it.hasNext(); ) {
                    N n = it.next();
                    if (visited.contains(n)) {
                        continue;
                    }
                    visited.add(n);
                    List<N> extendedPath = new ArrayList<>(path);
                    extendedPath.add(n);
                    newPaths.add(extendedPath);
                }
            }

            paths = newPaths;
        }

        return completedPaths;
    }

    public <N> List<List<N>> getAllUniquePaths(
            N start, Function<N, Iterator<N>> successors, Function<N, Boolean> success) {
        Set<List<N>> completedPaths = new HashSet<>();

        // Set<N> visited = new HashSet<>();

        Set<List<N>> paths = new HashSet<>();
        paths.add(new ArrayList<>(Collections.singletonList(start)));

        while (!paths.isEmpty()) {
            Set<List<N>> newPaths = new HashSet<>();
            for (List<N> path : paths) {
                N head = path.getLast();

                // Check if end has been reached
                if (success.apply(head)) {
                    completedPaths.add(path);
                    continue;
                }

                for (Iterator<N> it = successors.apply(head); it.hasNext(); ) {
                    N n = it.next();
                    List<N> extendedPath = new ArrayList<>(path);
                    extendedPath.add(n);
                    newPaths.add(extendedPath);
                }
            }

            paths = newPaths;
        }

        return completedPaths.stream().toList();
    }
}
