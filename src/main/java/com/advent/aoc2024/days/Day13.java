package com.advent.aoc2024.days;

import static java.lang.Math.abs;
import static java.lang.Math.round;

import com.advent.aoc2024.interfaces.BothParts;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class Day13 implements BothParts {

    private record XY(long x, long y) {}

    private record Machine(XY a, XY b, XY prize) {}

    private List<Machine> parseInput(String input) {
        Pattern number = Pattern.compile("\\d+");

        List<Long> numbers = new ArrayList<>();
        number.matcher(input).results().forEach(r -> numbers.add(Long.parseLong(r.group())));
        Iterator<Long> it = numbers.iterator();

        List<Machine> machines = new ArrayList<>();

        while (it.hasNext()) {
            machines.add(
                    new Machine(
                            new XY(it.next(), it.next()),
                            new XY(it.next(), it.next()),
                            new XY(it.next(), it.next())));
        }

        return machines;
    }

    private List<Machine> parseInput2(String input) {
        List<Machine> machines = parseInput(input);

        return machines.stream()
                .map(
                        m ->
                                new Machine(
                                        m.a,
                                        m.b,
                                        new XY(
                                                10000000000000L + m.prize.x,
                                                10000000000000L + m.prize.y)))
                .toList();
    }

    private Optional<double[]> solve(XY a, XY b, XY p) {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{a.x, b.x}, {a.y, b.y}});

        // Check determinant to avoid singular matrix
        double determinant = new LUDecomposition(matrix).getDeterminant();
        if (Math.abs(determinant) < 1e-10) {
            // The vectors are collinear. The matrix cannot be inverted.
            return Optional.empty();
        }

        RealMatrix inverse = new LUDecomposition(matrix).getSolver().getInverse();

        // Multiply the inverse matrix with vector p
        RealMatrix pMatrix = new Array2DRowRealMatrix(new double[] {p.x, p.y});
        RealMatrix resultMatrix = inverse.multiply(pMatrix);

        // Extract the multipliers as an array
        return Optional.of(resultMatrix.getColumn(0));
    }

    private Optional<Long> closeEnough(Double num) {
        if (abs(round(num) - num) < 0.01) {
            return Optional.of(round(num));
        } else {
            return Optional.empty();
        }
    }

    private long solveParts(List<Machine> machines) {
        return machines.stream()
                .map((Machine m) -> solve(m.a, m.b, m.prize))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToLong(
                        result -> {
                            Optional<Long> int_a = closeEnough(result[0]);
                            Optional<Long> int_b = closeEnough(result[1]);
                            if (int_a.isPresent() && int_b.isPresent()) {
                                return int_a.get() * 3 + int_b.get();
                            } else {
                                return 0;
                            }
                        })
                .sum();
    }

    @Override
    public Long part1(String input) {
        List<Machine> machines = parseInput(input);

        return solveParts(machines);
    }

    @Override
    public Object part2(String input) {
        List<Machine> machines = parseInput2(input);

        return solveParts(machines);
    }
}
