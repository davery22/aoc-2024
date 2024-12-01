package advent.of.code;

import advent.of.code.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day01 {
    public static void main(String[] args) throws Exception {
        var d = new Day01();
        d.part1();
        d.part2();
    }
    
    void part1() throws Exception {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        try (var lines = Utils.lines("/day01.txt")) {
            lines.forEach(line -> {
                String[] s = line.split("\\s+");
                list1.add(Integer.parseInt(s[0]));
                list2.add(Integer.parseInt(s[1]));
            });
        }
        list1.sort(Integer::compareTo);
        list2.sort(Integer::compareTo);
        long sum = 0;
        for (int i = 0; i < list1.size(); i++) {
            sum += Math.abs(list1.get(i) - list2.get(i));
        }
        System.out.println(sum);
    }
    
    void part2() throws Exception {
        List<Integer> list1 = new ArrayList<>();
        Map<Integer, Integer> freq2 = new HashMap<>();
        try (var lines = Utils.lines("/day01.txt")) {
            lines.forEach(line -> {
                String[] s = line.split("\\s+");
                list1.add(Integer.parseInt(s[0]));
                freq2.merge(Integer.parseInt(s[1]), 1, Integer::sum);
            });
        }
        long sum = 0;
        for (int n : list1) {
            sum += (long) n * freq2.getOrDefault(n, 0);
        }
        System.out.println(sum);
    }
}