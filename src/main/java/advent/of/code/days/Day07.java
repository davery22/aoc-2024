package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.Arrays;

public class Day07 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, false);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, true);
    }
    
    void solve(IO io, boolean allowConcat) {
        long sum = io.lines()
            .map(line -> Arrays.stream(line.split(":? ")).mapToLong(Long::parseLong).toArray())
            .filter(numbers -> canEqualTarget(numbers[0], numbers[1], numbers, 2, allowConcat))
            .mapToLong(numbers -> numbers[0])
            .sum();
        io.out().println(sum);
    }
    
    boolean canEqualTarget(long target, long n, long[] numbers, int i, boolean allowConcat) {
        if (i == numbers.length) {
            return n == target;
        }
        return canEqualTarget(target, n * numbers[i], numbers, i+1, allowConcat)
            || canEqualTarget(target, n + numbers[i], numbers, i+1, allowConcat)
            || allowConcat && canEqualTarget(target, concat(n, numbers[i]), numbers, i+1, true);
    }
    
    long concat(long a, long b) {
        for (long t = b; t > 0; t /= 10) {
            a *= 10;
        }
        return a + b;
    }
}
