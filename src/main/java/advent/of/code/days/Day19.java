package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day19 implements Day {
    @Override
    public void part1(IO io) {
        Scanner s = io.scanner();
        String[] choices = s.nextLine().split(", ");
        String[] designs = s.useDelimiter(IO.EOL).tokens().toArray(String[]::new);
        Map<String, Boolean> memo = new HashMap<>();
        int count = 0;
        for (String design : designs) {
            if (isPossible(design, choices, memo)) {
                count++;
            }
        }
        io.out().println(count);
    }
    
    @Override
    public void part2(IO io) {
        Scanner s = io.scanner();
        String[] choices = s.nextLine().split(", ");
        String[] designs = s.useDelimiter(IO.EOL).tokens().toArray(String[]::new);
        Map<String, Long> memo = new HashMap<>();
        long sum = 0;
        for (String design : designs) {
            sum += countWays(design, choices, memo);
        }
        io.out().println(sum);
    }
    
    boolean isPossible(String design, String[] choices, Map<String, Boolean> memo) {
        if (design.isEmpty()) {
            return true;
        }
        Boolean b = memo.get(design);
        if (b != null) {
            return b;
        }
        for (String choice : choices) {
            if (design.startsWith(choice) && isPossible(design.substring(choice.length()), choices, memo)) {
                memo.put(design, true);
                return true;
            }
        }
        memo.put(design, false);
        return false;
    }
    
    long countWays(String design, String[] choices, Map<String, Long> memo) {
        if (design.isEmpty()) {
            return 1;
        }
        Long i = memo.get(design);
        if (i != null) {
            return i;
        }
        long sum = 0;
        for (String choice : choices) {
            if (design.startsWith(choice)) {
                sum += countWays(design.substring(choice.length()), choices, memo);
            }
        }
        memo.put(design, sum);
        return sum;
    }
}
