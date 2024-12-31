package com.advent.aoc2024.utils;

import java.util.*;

public class GraphUtils {

    public static <N extends Comparable<N>> List<TreeSet<N>> getAllMaximalCliques(
            Map<N, TreeSet<N>> graph) {
        List<TreeSet<N>> cliques = new ArrayList<>();

        getAllMaximalCliques(
                graph, new TreeSet<>(), new TreeSet<>(graph.keySet()), new TreeSet<>(), cliques);

        return cliques;
    }

    public static <N extends Comparable<N>> void getAllMaximalCliques(
            Map<N, TreeSet<N>> graph,
            TreeSet<N> r,
            TreeSet<N> p,
            TreeSet<N> x,
            List<TreeSet<N>> cliques) {
        // Using the Bron-Kerbosch algorithm

        if (p.isEmpty() && x.isEmpty()) {

            TreeSet<N> rCopy = new TreeSet<>();
            rCopy.addAll(r);
            cliques.add(rCopy);
        }

        for (N node : List.copyOf(p)) {
            List<N> neighbors = graph.get(node).stream().toList();
            TreeSet<N> newR = new TreeSet<>();
            newR.addAll(r);
            newR.add(node);

            TreeSet<N> newP = new TreeSet<>();
            p.stream().filter(neighbors::contains).forEach(newP::add);

            TreeSet<N> newX = new TreeSet<>();
            x.stream().filter(neighbors::contains).forEach(newX::add);

            getAllMaximalCliques(graph, newR, newP, newX, cliques);

            p.remove(node);
            x.add(node);
        }
    }
}
