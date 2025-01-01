package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Day24 implements Day {
    private record Gate(String input1, String input2, String op, String output) {}

    private record Input(Map<String, Boolean> inputStates, List<Gate> gates) {}

    private final Pattern GATE =
            Pattern.compile("([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)");

    private Input parse(String input) {
        String[] sections = input.split("\n\n");

        HashMap<String, Boolean> inputStates = new HashMap<>();
        for (String line : sections[0].split("\n")) {
            String id = line.substring(0, 3);
            Boolean state = line.charAt(5) == '1';
            inputStates.put(id, state);
        }

        ArrayList<Gate> gates = new ArrayList<>();
        GATE.matcher(sections[1])
                .results()
                .forEach(r -> gates.add(new Gate(r.group(1), r.group(3), r.group(2), r.group(4))));

        return new Input(inputStates, gates);
    }

    private long run(Map<String, Boolean> initialStates, List<Gate> gates) {
        Map<String, Boolean> states =
                initialStates.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Gate> pendingGates = new ArrayList<>(gates);

        Predicate<Gate> inputsAreAvailable =
                gate -> states.containsKey(gate.input1) && states.containsKey(gate.input2);
        Supplier<Optional<Gate>> nextGate =
                () -> pendingGates.stream().filter(inputsAreAvailable).findFirst();

        Optional<Gate> maybePendingGate = nextGate.get();

        while (maybePendingGate.isPresent()) {
            Gate gate = maybePendingGate.get();
            Boolean out =
                    switch (gate.op) {
                        case "AND" -> states.get(gate.input1) & states.get(gate.input2);
                        case "OR" -> states.get(gate.input1) | states.get(gate.input2);
                        case "XOR" -> states.get(gate.input1) ^ states.get(gate.input2);
                        default -> throw new RuntimeException("Unknown operation: " + gate.op);
                    };
            states.put(gate.output, out);
            pendingGates.remove(gate);

            maybePendingGate = nextGate.get();
        }

        List<Long> bitValues =
                states.entrySet().stream()
                        .filter(e -> e.getKey().startsWith("z"))
                        .sorted(Map.Entry.comparingByKey())
                        .mapToLong(e -> e.getValue() ? 1 : 0)
                        .boxed()
                        .toList();

        return bitValues.reversed().stream().reduce(0L, (sum, val) -> (sum << 1) + val);
    }

    @Override
    public Long part1(String inputStr) {
        Input input = parse(inputStr);

        return run(input.inputStates, input.gates);
    }

    @Override
    public String part2(String input) {
        // The gates form a ripple-carry adder.
        //
        // The first block is a half-adder which takes only x00 and y01
        // as the input, and outputs z00 and a carry bit "c01".
        //
        // The following blocks are full-adders which take inputs xn, yn,
        // and "c(n-1)", and output zn and a carry bit "cn".
        //
        // Grouping the XOR, XOR, AND, AND, and OR operations of each block
        // makes it easy to spot the gates which are not wired correctly.
        return "Just solve this one manually. See the code comment for help.";
    }
}
