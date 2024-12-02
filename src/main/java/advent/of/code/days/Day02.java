package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.stream.Stream;

public class Day02 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, false);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, true);
    }
    
    void solve(IO io, boolean tolerateOne) {
        var count = io.lines()
            .map(line -> Stream.of(line.split("\\s+")).mapToInt(Integer::parseInt).toArray())
            .filter(report -> isSafe(report, tolerateOne))
            .count();
        io.out().println(count);
    }
    
    boolean isSafe(int[] report, boolean tolerateOne) {
        return isSafe(report, true, tolerateOne)
            || isSafe(report, false, tolerateOne);
    }
    
    boolean isSafe(int[] report, boolean inc, boolean tolerateOne) {
        for (int i = 1; i < report.length; i++) {
            if (check(inc, report[i-1], report[i])) {
                continue;
            }
            // Part 2 only
            if (tolerateOne) {
                tolerateOne = false;
                if (i == report.length-1 || check(inc, report[i-1], report[i+1])) {
                    // Remove i
                    i++;
                    continue;
                }
                if (i == 1 || check(inc, report[i-2], report[i])) {
                    // Remove i-1
                    continue;
                }
            }
            return false;
        }
        return true;
    }
    
    boolean check(boolean inc, int a, int b) {
        return inc
            ? a < b && b-a <= 3
            : b < a && a-b <= 3;
    }
}
