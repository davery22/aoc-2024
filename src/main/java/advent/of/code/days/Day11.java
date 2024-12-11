package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.stream.Collectors;

public class Day11 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, 25);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, 75);
    }
    
    void solve(IO io, int blinks) {
        Map<Long, Long> curr = io.scanner().tokens().map(Long::parseLong).collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        for (int i = 0; i < blinks; i++) {
            Map<Long, Long> next = new HashMap<>();
            curr.forEach((stone, count) -> {
                String s;
                if (stone == 0) {
                    next.merge(1L, count, Long::sum);
                } else if ((s = Long.toString(stone)).length() % 2 == 0) {
                    next.merge(Long.parseLong(s.substring(0, s.length()/2)), count, Long::sum);
                    next.merge(Long.parseLong(s.substring(s.length()/2)), count, Long::sum);
                } else {
                    next.merge(stone * 2024, count, Long::sum);
                }
            });
            curr = next;
        }
        io.out().println(curr.values().stream().mapToLong(i -> i).sum());
    }
}
