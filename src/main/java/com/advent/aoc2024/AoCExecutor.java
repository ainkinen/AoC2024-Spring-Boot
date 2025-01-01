package com.advent.aoc2024;

import com.advent.aoc2024.interfaces.Day;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

@Component
public class AoCExecutor {
    private static final Logger logger = LogManager.getLogger();

    private final ApplicationContext applicationContext;
    private final ApplicationArguments applicationArguments;
    private final InputReader inputReader;

    @Autowired
    public AoCExecutor(
            ApplicationContext applicationContext,
            ApplicationArguments applicationArguments,
            InputReader reader) {
        this.applicationContext = applicationContext;
        this.applicationArguments = applicationArguments;
        this.inputReader = reader;
    }

    private static void execPart(
            String name, String partName, Function<String, Object> part, String input) {
        long start = System.nanoTime();
        Object result = part.apply(input);
        long end = System.nanoTime();
        logger.info("{} - {}: {}", name, partName, result);

        Duration duration = Duration.ofNanos(end - start);
        long seconds = duration.getSeconds();
        long millis = duration.toMillisPart();
        long micros = (duration.toNanosPart() / 1000) % 1000;
        long nanos = duration.toNanosPart() % 1000;
        logger.info("\tDuration: {} s {} ms {} μs {} ns", seconds, millis, micros, nanos);
    }

    public void run() {
        if (applicationArguments.containsOption("day")) {
            runOne(applicationArguments.getOptionValues("day").getFirst());
        } else {
            runAll();
        }
    }

    private void runDay(Day day) {
        String name = day.getClass().getSimpleName();

        String input = this.inputReader.read(name);

        execPart(name, "Part 1", day::part1, input);

        execPart(name, "Part 2", day::part2, input);
    }

    private String getDay(String input) {
        return String.format("day%02d", Integer.parseInt(input));
    }

    private void runOne(String dayInput) {
        Day day = applicationContext.getBean(getDay(dayInput), Day.class);

        runDay(day);
    }

    private void runAll() {
        List<Day> days = applicationContext.getBeansOfType(Day.class).values().stream().toList();

        for (Day day : days) {
            runDay(day);
        }
    }
}
