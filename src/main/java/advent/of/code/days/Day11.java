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
        // First discover all the progeny, so that we can apply bottom-up dynamic programming.
        List<Long> initialStones = io.scanner().tokens().map(Long::parseLong).toList(), curr = new ArrayList<>(initialStones);
        Map<Long, List<Long>> childrenByParent = new HashMap<>();
        for (int i = 0; i < blinks; i++) {
            List<Long> next = new ArrayList<>();
            for (long stone : curr) {
                if (childrenByParent.getOrDefault(stone, List.of()).isEmpty()) {
                    List<Long> children = blink(stone);
                    childrenByParent.put(stone, children);
                    for (long child : children) {
                        if (childrenByParent.putIfAbsent(child, List.of()) == null) {
                            next.add(child);
                        }
                    }
                }
            }
            curr = next;
        }
        // Good to go.
        Map<Long, long[]> memos = childrenByParent.keySet().stream().collect(Collectors.toMap(s -> s, _ -> new long[]{ 1, 0 }));
        for (int i = 0; i < blinks; i++) {
            childrenByParent.forEach((parent, children) ->
                memos.get(parent)[1] = children.stream().mapToLong(child -> memos.get(child)[0]).sum()
            );
            memos.forEach((_, memo) -> memo[0] = memo[1]);
        }
        long sum = initialStones.stream().mapToLong(s -> memos.get(s)[0]).sum();
        io.out().println(sum);
    }
    
    List<Long> blink(long stone) {
        if (stone == 0) {
            return List.of(1L);
        }
        String s = Long.toString(stone);
        if (s.length() % 2 == 0) {
            return List.of(Long.parseLong(s.substring(0, s.length()/2)), Long.parseLong(s.substring(s.length()/2)));
        }
        return List.of(stone * 2024);
    }
}
