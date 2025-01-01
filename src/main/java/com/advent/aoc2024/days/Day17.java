package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.Day;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Day17 implements Day {
    private static class Computer {

        long a;
        long b;
        long c;

        List<Integer> prog;
        int counter;

        public Computer(long a, long b, long c, List<Integer> prog, int counter) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.prog = prog;
            this.counter = counter;
        }

        public long combo(long n) {
            if (n < 4) {
                return n;
            } else if (n == 4) {
                return this.a;
            } else if (n == 5) {
                return this.b;
            } else if (n == 6) {
                return this.c;
            } else {
                throw new RuntimeException("Invalid number");
            }
        }

        public Optional<Long> nextOutput() {
            while (this.counter <= this.prog.size() - 2) {
                int op = this.prog.get(this.counter);
                int operand = this.prog.get(this.counter + 1);

                Optional<Long> out = Optional.empty();

                switch (op) {
                    case 0:
                        {
                            // adv
                            this.a >>= this.combo(operand);
                            break;
                        }
                    case 1:
                        {
                            // bxl
                            this.b ^= operand;
                            break;
                        }
                    case 2:
                        {
                            // bst
                            this.b = this.combo(operand) & 0b111;
                            break;
                        }
                    case 3:
                        {
                            // jnz
                            if (this.a != 0) {
                                this.counter = operand;
                                continue;
                            }
                            break;
                        }
                    case 4:
                        {
                            // bxc
                            this.b ^= this.c;
                            break;
                        }
                    case 5:
                        {
                            // out
                            out = Optional.of(this.combo(operand) & 0b111);
                            break;
                        }
                    case 6:
                        {
                            // bdv
                            this.b = this.a >> this.combo(operand);
                            break;
                        }
                    case 7:
                        {
                            // cdv
                            this.c = this.a >> this.combo(operand);
                            break;
                        }
                    default:
                        throw new RuntimeException("Unknown op: " + op);
                }

                this.counter += 2;
                if (out.isPresent()) {
                    return out;
                }
            }

            return Optional.empty();
        }

        public List<Long> getOutput() {
            List<Long> output = new ArrayList<>();

            Optional<Long> next = this.nextOutput();
            while (next.isPresent()) {
                output.add(next.get());
                next = this.nextOutput();
            }

            return output;
        }
    }

    private <T> List<T> drainToList(Iterator<T> it) {
        List<T> list = new ArrayList<>();

        while (it.hasNext()) {
            list.add(it.next());
        }

        return list;
    }

    private <T> boolean listStartsWithOther(List<T> list, List<T> other) {
        Iterator<T> itList = list.iterator();
        Iterator<T> itOther = other.iterator();

        while (itOther.hasNext() && itList.hasNext()) {
            if (itOther.next() != itList.next()) {
                return false;
            }
        }

        return true;
    }

    private Computer parse(String input) {
        Pattern numberPattern = Pattern.compile("\\d+");

        Iterator<Integer> numbers =
                numberPattern
                        .matcher(input)
                        .results()
                        .map(r -> Integer.valueOf(r.group()))
                        .iterator();

        return new Computer(
                numbers.next(), numbers.next(), numbers.next(), drainToList(numbers), 0);
    }

    @Override
    public String part1(String input) {
        Computer computer = parse(input);
        List<Long> output = computer.getOutput();
        return output.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public Long part2Brute(String input) {
        Computer parsed = parse(input);

        List<Long> longProg = parsed.prog.stream().map(Long::valueOf).toList();

        for (long i = 0; i < 1174400; i++) {
            Computer comp = new Computer(i, parsed.b, parsed.c, parsed.prog, 0);
            List<Long> output = comp.getOutput();
            if (output.equals(longProg)) {
                return i;
            }
        }

        throw new RuntimeException("No solution found");
    }

    @Override
    public Long part2(String input) {
        Computer parsedComputer = parse(input);

        List<Long> reversedInitialProgram =
                parsedComputer.prog.stream().map(Long::valueOf).toList().reversed();

        // Analyzing the code shows that the loop works over reg_a in 3 bit chunks.
        // Every chunk is used to generate one output, but previous numbers also affect the output.

        long needle = 0;

        outer:
        for (long x = 0; x < parsedComputer.prog.size(); x++) {
            for (long chunk = 0; chunk < 70; chunk++) {
                Computer comp =
                        new Computer(
                                needle + chunk,
                                parsedComputer.b,
                                parsedComputer.c,
                                parsedComputer.prog,
                                0);

                List<Long> output = comp.getOutput();
                List<Long> reversedOutput = output.reversed();

                boolean endsMatch = listStartsWithOther(reversedInitialProgram, reversedOutput);
                if (endsMatch) {

                    if (output.size() == parsedComputer.prog.size()) {
                        return needle + chunk;
                    }

                    needle = (needle + chunk) << 3;

                    continue outer;
                }
            }
        }

        throw new RuntimeException("No solution found");
    }
}
