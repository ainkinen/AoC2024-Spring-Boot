package com.advent.aoc2024.days;

import com.advent.aoc2024.interfaces.BothParts;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.LongStream;

@Component
public class Day09 implements BothParts {
    private record Block(Optional<Integer> id) {}

    private class Allocation {

        protected Optional<Integer> id;
        protected int length;
        protected boolean moved;

        Allocation(Optional<Integer> id, int length, boolean moved) {
            this.id = id;
            this.length = length;
            this.moved = moved;
        }
    }

    private ArrayList<Block> parseBlocks(String input) {
        // Make sure that input can be processed as pairs of file & empty
        String fixed = input;
        if (fixed.length() % 2 != 0) {
            fixed += "0";
        }

        ArrayList<Block> blocks = new ArrayList<>();
        for (int i = 0; i * 2 < fixed.length() - 1; i++) {
            int file_len = Character.getNumericValue(fixed.charAt(i * 2));
            for (int time = 0; time < file_len; time++) {
                blocks.add(new Block(Optional.of(i)));
            }

            int empty_len = Character.getNumericValue(fixed.charAt((i * 2) + 1));
            for (int time = 0; time < empty_len; time++) {
                blocks.add(new Block(Optional.empty()));
            }
        }

        return blocks;
    }

    @SuppressWarnings("unused")
    private void printBlocks(List<Block> blocks) {
        for (Block block : blocks) {
            if (block.id.isPresent()) {
                System.out.print(block.id.get());
            } else {
                System.out.print(".");
            }
        }
        System.out.println();
    }

    private ArrayList<Allocation> parseAllocations(String input) {
        // Make sure that input can be processed as pairs of file & empty
        String fixed = input;
        if (fixed.length() % 2 != 0) {
            fixed += "0";
        }

        ArrayList<Allocation> allocations = new ArrayList<>();
        for (int id = 0; id * 2 < fixed.length() - 1; id++) {
            int file_len = Character.getNumericValue(fixed.charAt(id * 2));
            allocations.add(new Allocation(Optional.of(id), file_len, false));

            int empty_len = Character.getNumericValue(fixed.charAt((id * 2) + 1));
            allocations.add(new Allocation(Optional.empty(), empty_len, true));
        }

        return allocations;
    }

    @SuppressWarnings("unused")
    private String toString(List<Allocation> allocations) {
        StringBuilder str = new StringBuilder();
        for (Allocation alloc : allocations) {
            String alloc_str = alloc.id.map(Object::toString).orElse(".").repeat(alloc.length);
            str.append(alloc_str);
        }
        return str.toString();
    }

    private void deFrag(List<Block> blocks) {
        int front_pointer = 0;
        int end_pointer = blocks.size() - 1;

        // Find first positions
        while (blocks.get(front_pointer).id.isPresent()) {
            front_pointer++;
        }
        while (blocks.get(end_pointer).id.isEmpty()) {
            end_pointer--;
        }

        while (front_pointer < end_pointer) {
            // Swap
            Collections.swap(blocks, front_pointer, end_pointer);

            // Move pointers again
            while (blocks.get(front_pointer).id.isPresent()) {
                front_pointer++;
            }
            while (blocks.get(end_pointer).id.isEmpty()) {
                end_pointer--;
            }
        }
    }

    private Optional<Integer> findAvailableSpot(
            List<Allocation> allocations, int minSize, int max) {
        for (int i = 0; i < allocations.size() && i < max; i++) {
            Allocation allocation = allocations.get(i);
            if (allocation.id.isEmpty() && allocation.length >= minSize) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> nextFileToMove(List<Allocation> allocations) {
        for (int i = allocations.size() - 1; i >= 0; i--) {
            Allocation allocation = allocations.get(i);
            if (!allocation.moved) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private void deFragAllocations(List<Allocation> allocations) {
        Optional<Integer> maybeToMove = nextFileToMove(allocations);
        while (maybeToMove.isPresent()) {
            int toMove = maybeToMove.get();

            allocations.get(toMove).moved = true;

            Optional<Integer> maybeFreeSpace =
                    findAvailableSpot(allocations, allocations.get(toMove).length, toMove);

            if (maybeFreeSpace.isPresent()) {
                int freeSpace = maybeFreeSpace.get();

                if (allocations.get(toMove).length == allocations.get(freeSpace).length) {
                    Collections.swap(allocations, toMove, freeSpace);
                } else {
                    allocations.get(freeSpace).length -= allocations.get(toMove).length;
                    allocations.add(
                            freeSpace,
                            new Allocation(Optional.empty(), allocations.get(toMove).length, true));

                    Collections.swap(allocations, toMove + 1, freeSpace);
                }
            }

            // compact the end
            if (allocations.getLast().id.isEmpty()) {
                allocations.removeLast();
            }

            maybeToMove = nextFileToMove(allocations);
        }
    }

    @Override
    public Long part1(String input) {
        List<Block> blocks = parseBlocks(input);

        deFrag(blocks);

        return LongStream.range(0, blocks.size() - 1)
                .map(i -> i * blocks.get((int) i).id.orElse(0))
                .sum();
    }

    @Override
    public Long part2(String input) {
        ArrayList<Allocation> allocations = parseAllocations(input);

        deFragAllocations(allocations);

        long total = 0;

        int pointer = 0;
        for (Allocation alloc : allocations) {
            for (int i = 0; i < alloc.length; i++) {
                total += (long) (pointer + i) * alloc.id.orElse(0);
            }
            pointer = pointer + alloc.length;
        }

        return total;
    }
}
