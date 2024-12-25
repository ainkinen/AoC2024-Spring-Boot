package com.advent.aoc2024;

import com.advent.aoc2024.interfaces.BothParts;
import com.advent.aoc2024.interfaces.Day;
import com.advent.aoc2024.interfaces.OnePartOnly;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

@Component
public class AoCExecutor {
    private static final Logger logger = LogManager.getLogger();

    private final InputReader inputReader;
    private final ApplicationContext applicationContext;

    @Autowired
    public AoCExecutor(InputReader reader, ApplicationContext applicationContext) {
        this.inputReader = reader;
        this.applicationContext = applicationContext;
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
        List<Day> days = applicationContext.getBeansOfType(Day.class).values().stream().toList();

        for (Day day : days) {

            String name = day.getClass().getSimpleName();

            String input = this.inputReader.read(name);

            if (day instanceof OnePartOnly onePartOnly) {
                execPart(name, "Part 1", onePartOnly::part1, input);
            }

            if (day instanceof BothParts completeDay) {
                execPart(name, "Part 2", completeDay::part2, input);
            }
        }
    }
}
