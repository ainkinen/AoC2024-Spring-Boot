package com.advent.aoc2024.days;

import static com.advent.aoc2024.utils.GraphUtils.getAllMaximalCliques;

import com.advent.aoc2024.interfaces.BothParts;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Day23 implements BothParts {

    private Map<String, TreeSet<String>> parseGraph(String input) {
        String[] lines = input.split("\n");

        Map<String, TreeSet<String>> graph = new HashMap<>();

        for (String line : lines) {
            String left = line.substring(0, 2);
            String right = line.substring(3);

            // Non-directional graph
            graph.computeIfAbsent(left, k -> new TreeSet<>()).add(right);
            graph.computeIfAbsent(right, k -> new TreeSet<>()).add(left);
        }

        return graph;
    }

    @Override
    public Integer part1(String inputStr) {
        Map<String, TreeSet<String>> graph = parseGraph(inputStr);
        List<String> tNodes = graph.keySet().stream().filter(k -> k.startsWith("t")).toList();

        HashSet<List<String>> triples = new HashSet<>();

        for (String a : tNodes) {
            TreeSet<String> bs = graph.get(a);
            for (String b : bs) {
                TreeSet<String> cs = graph.get(b);

                for (String c : cs) {
                    if (graph.get(a).contains(c)) {
                        List<String> list = Arrays.asList(a, b, c);
                        Collections.sort(list);
                        triples.add(list);
                    }
                }
            }
        }

        return triples.size();
    }

    @Override
    public String part2(String inputStr) {
        Map<String, TreeSet<String>> graph = parseGraph(inputStr);

        List<TreeSet<String>> cliques = getAllMaximalCliques(graph);

        TreeSet<String> largestNetwork =
                cliques.stream().max(Comparator.comparingInt(TreeSet::size)).orElseThrow();

        return String.join(",", largestNetwork);
    }
}
