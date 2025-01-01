package com.advent.aoc2024.days;

import static java.lang.Math.sqrt;

import com.advent.aoc2024.interfaces.Day;
import com.advent.aoc2024.utils.Coord;
import com.advent.aoc2024.utils.CoordDelta;

import org.apache.commons.lang3.IntegerRange;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day14 implements Day {

    private record Robot(Coord loc, CoordDelta delta) {}

    private List<Robot> parseRobots(String input) {
        Pattern number = Pattern.compile("-?\\d+");

        List<Integer> numbers = new ArrayList<>();
        number.matcher(input).results().forEach(r -> numbers.add(Integer.parseInt(r.group())));
        Iterator<Integer> it = numbers.iterator();

        List<Robot> robots = new ArrayList<>();
        while (it.hasNext()) {
            robots.add(
                    new Robot(
                            new Coord(it.next(), it.next()), new CoordDelta(it.next(), it.next())));
        }

        return robots;
    }

    private Robot step(Robot robot, int times, int max_x, int max_y) {
        CoordDelta jump = robot.delta.times(times);
        return new Robot(robot.loc.plus(jump).mod(max_x, max_y), robot.delta);
    }

    @SuppressWarnings("unused")
    private void graphRobots(List<Robot> robots, int len_x, int len_y) {
        Map<Coord, Long> robotsAtLocs =
                robots.stream()
                        .map(Robot::loc)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int y = 0; y < len_y; y++) {
            for (int x = 0; x < len_x; x++) {
                Coord at = new Coord(x, y);
                if (robotsAtLocs.containsKey(at)) {
                    System.out.print(robotsAtLocs.get(at));
                } else {
                    System.out.print(".");
                }
            }

            System.out.println();
        }
    }

    private List<Robot> stepRobots(List<Robot> robots, int steps, int len_x, int len_y) {
        return robots.stream().map(r -> step(r, steps, len_x, len_y)).toList();
    }

    public Long solver(String input, int steps, int len_x, int len_y) {
        List<Robot> robots = parseRobots(input);

        robots = stepRobots(robots, steps, len_x, len_y);

        long nw = 0;
        long ne = 0;
        long se = 0;
        long sw = 0;

        int middle_x = len_x / 2;
        int middle_y = len_y / 2;

        IntegerRange w_x = IntegerRange.of(0, middle_x - 1);
        IntegerRange e_x = IntegerRange.of(middle_x + 1, len_x);
        IntegerRange n_y = IntegerRange.of(0, middle_y - 1);
        IntegerRange s_y = IntegerRange.of(middle_y + 1, len_y);

        for (Robot robot : robots) {
            Coord loc = robot.loc;
            if (loc.inRange(w_x, n_y)) {
                nw++;
            } else if (loc.inRange(e_x, n_y)) {
                ne++;
            } else if (loc.inRange(e_x, s_y)) {
                se++;
            } else if (loc.inRange(w_x, s_y)) {
                sw++;
            }
        }

        return nw * ne * se * sw;
    }

    @Override
    public Object part1(String input) {
        return solver(input, 100, 101, 103);
    }

    private Optional<double[]> mean(List<Coord> data) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        int size = data.size();

        double mean_x = data.stream().mapToDouble(Coord::x).sum() / size;
        double mean_y = data.stream().mapToDouble(Coord::y).sum() / size;

        return Optional.of(new double[] {mean_x, mean_y});
    }

    private Optional<Double> stdDev(List<Coord> data) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        Optional<double[]> maybeMean = mean(data);
        if (maybeMean.isEmpty()) {
            return Optional.empty();
        }
        double[] meanPoint = maybeMean.get();

        double variance =
                data.stream()
                                .mapToDouble(
                                        coord -> {
                                            // Compute Euclidean distance from the mean point
                                            double dx = coord.x() - meanPoint[0];
                                            double dy = coord.y() - meanPoint[1];
                                            return dx * dx + dy * dy;
                                        })
                                .sum()
                        / data.size();

        return Optional.of(sqrt(variance));
    }

    private int findPossibleImage(List<Robot> robots, int len_x, int len_y) {
        // Image should have some structure
        // Find the step with smallest std deviation

        double[] deviations =
                IntStream.range(0, 10000)
                        .asLongStream()
                        .mapToDouble(
                                steps -> {
                                    List<Robot> rs = stepRobots(robots, (int) steps, len_x, len_y);
                                    List<Coord> locs = rs.stream().map(Robot::loc).toList();
                                    return stdDev(locs).orElseThrow();
                                })
                        .toArray();

        int min_idx = 0;
        double min_dev = Double.MAX_VALUE;

        for (int i = 0; i < deviations.length; i++) {
            double dev = deviations[i];
            if (dev < min_dev) {
                min_dev = dev;
                min_idx = i;
            }
        }
        return min_idx;
    }

    public int solverPart2(String input, int len_x, int len_y) {
        List<Robot> robots = parseRobots(input);

        //noinspection UnnecessaryLocalVariable
        int possibleImageIdx = findPossibleImage(robots, len_x, len_y);

        //        robots = stepRobots(robots, possibleImageIdx, len_x, len_y);
        //        graphRobots(robots, len_x, len_y);

        return possibleImageIdx;
    }

    @Override
    public Integer part2(String input) {
        return solverPart2(input, 101, 103);
    }
}
