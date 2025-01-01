package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;
import com.advent.aoc2024.utils.CompassDirection;
import com.advent.aoc2024.utils.CoordDelta;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day12 implements Day {
    private record Node(int x, int y, char id) {}

    private record Input(
            Set<Node> nodes, HashMap<Node, List<Node>> edges, HashMap<Node, Integer> corners) {}

    private Input parseInput(String input) {
        HashSet<Node> nodes = new HashSet<>();

        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                nodes.add(new Node(x, y, line.charAt(x)));
            }
        }

        HashMap<Node, List<Node>> edges = new HashMap<>();
        HashMap<Node, Integer> corners = new HashMap<>();

        for (Node fromNode : nodes) {
            int x = fromNode.x;
            int y = fromNode.y;
            char id = fromNode.id;

            Function<CompassDirection, Optional<Node>> dirToNode =
                    dir -> {
                        CoordDelta d = dir.asCoordDelta();
                        Node n = new Node(x + d.x(), y + d.y(), id);
                        if (nodes.contains(n)) {
                            return Optional.of(n);
                        } else {
                            return Optional.empty();
                        }
                    };

            Optional<Node> N = dirToNode.apply(CompassDirection.N);
            Optional<Node> NE = dirToNode.apply(CompassDirection.NE);
            Optional<Node> E = dirToNode.apply(CompassDirection.E);
            Optional<Node> SE = dirToNode.apply(CompassDirection.SE);
            Optional<Node> S = dirToNode.apply(CompassDirection.S);
            Optional<Node> SW = dirToNode.apply(CompassDirection.SW);
            Optional<Node> W = dirToNode.apply(CompassDirection.W);
            Optional<Node> NW = dirToNode.apply(CompassDirection.NW);

            List<Optional<Node>> neighbors = List.of(N, S, W, E);

            for (Optional<Node> neighbor : neighbors) {
                neighbor.ifPresent(
                        toNode ->
                                edges.merge(
                                        fromNode,
                                        List.of(toNode),
                                        (a, b) -> Stream.concat(a.stream(), b.stream()).toList()));
            }

            // Convex corners
            if (N.isEmpty() && W.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }
            if (N.isEmpty() && E.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }
            if (E.isEmpty() && S.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }
            if (S.isEmpty() && W.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }

            // Concave corners
            if (N.isPresent() && E.isPresent() && NE.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }
            if (E.isPresent() && S.isPresent() && SE.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }
            if (S.isPresent() && W.isPresent() && SW.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }
            if (N.isPresent() && W.isPresent() && NW.isEmpty()) {
                corners.merge(fromNode, 1, Integer::sum);
            }
        }

        return new Input(nodes, edges, corners);
    }

    private Set<Node> getConnectedNodes(Input input, Node start) {
        Set<Node> graph = new HashSet<>();
        graph.add(start);
        Set<Node> heads = new HashSet<>(graph);

        while (!heads.isEmpty()) {
            Set<Node> nextHeads =
                    heads.stream()
                            .flatMap(
                                    n ->
                                            input
                                                    .edges
                                                    .getOrDefault(n, Collections.emptyList())
                                                    .stream())
                            .filter(n -> !graph.contains(n))
                            .collect(Collectors.toSet());
            graph.addAll(nextHeads);
            heads = nextHeads;
        }

        return graph;
    }

    private List<Set<Node>> getPlots(Input input) {
        List<Set<Node>> plots = new ArrayList<>();

        Set<Node> nodesLeft = new HashSet<>(input.nodes);

        while (!nodesLeft.isEmpty()) {
            Node node = nodesLeft.iterator().next();

            Set<Node> plot = getConnectedNodes(input, node);
            nodesLeft.removeAll(plot);
            plots.add(plot);
        }

        return plots;
    }

    private int getPerimeter(Set<Node> plot, Input input) {
        return plot.stream()
                .map((Node node) -> input.edges.getOrDefault(node, List.of()))
                .mapToInt(edges -> 4 - edges.size())
                .sum();
    }

    private int getSides(Set<Node> plot, Input input) {
        return plot.stream().mapToInt(node -> input.corners.getOrDefault(node, 0)).sum();
    }

    @Override
    public Integer part1(String inputStr) {
        Input input = parseInput(inputStr);

        List<Set<Node>> plots = getPlots(input);

        return plots.stream().mapToInt(plot -> plot.size() * getPerimeter(plot, input)).sum();
    }

    @Override
    public Integer part2(String inputStr) {
        Input input = parseInput(inputStr);

        List<Set<Node>> plots = getPlots(input);

        return plots.stream().mapToInt(plot -> plot.size() * getSides(plot, input)).sum();
    }
}
