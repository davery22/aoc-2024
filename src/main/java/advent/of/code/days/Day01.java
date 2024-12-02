package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;

public class Day01 implements Day {
    @Override
    public void part1(IO io) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (Scanner s = io.scanner(); s.hasNextLine(); ) {
            list1.add(s.nextInt());
            list2.add(s.nextInt());
        }
        list1.sort(Integer::compareTo);
        list2.sort(Integer::compareTo);
        int sum = 0;
        for (int i = 0; i < list1.size(); i++) {
            sum += Math.abs(list1.get(i) - list2.get(i));
        }
        io.out().println(sum);
    }
    
    @Override
    public void part2(IO io) {
        Map<Integer, Integer> freq1 = new HashMap<>();
        Map<Integer, Integer> freq2 = new HashMap<>();
        for (Scanner s = io.scanner(); s.hasNextLine(); ) {
            freq1.merge(s.nextInt(), 1, Integer::sum);
            freq2.merge(s.nextInt(), 1, Integer::sum);
        }
        int sum = 0;
        for (var e : freq1.entrySet()) {
            int k = e.getKey(), v = e.getValue();
            sum += k * v * freq2.getOrDefault(k, 0);
        }
        io.out().println(sum);
    }
}