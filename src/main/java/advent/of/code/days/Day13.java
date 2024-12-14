package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 implements Day {
    static final Pattern PATTERN = Pattern.compile(".+?(\\d+)".repeat(2));
    
    @Override
    public void part1(IO io) {
        solve(io, 0);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, 10_000_000_000_000L);
    }
    
    void solve(IO io, long offset) {
        long tokens = 0;
        for (Scanner s = io.scanner();; s.nextLine()) {
            Pos a = parseXY(s.nextLine(), 0);
            Pos b = parseXY(s.nextLine(), 0);
            Pos p = parseXY(s.nextLine(), offset);
            tokens += cost(a, b, p);
            if (!s.hasNextLine()) {
                break;
            }
        }
        io.out().println(tokens);
    }
    
    static class Pos {
        long x, y;
        Pos(long x, long y) { this.x = x; this.y = y; }
    }
    
    Pos parseXY(String line, long offset) {
        Matcher m = PATTERN.matcher(line);
        if (m.find()) {
            return new Pos(Long.parseLong(m.group(1))+offset, Long.parseLong(m.group(2))+offset);
        }
        throw new IllegalStateException();
    }
    
    long cost(Pos a, Pos b, Pos p) {
        // This would be so much more interesting if the divisors could be 0.
        // (ie infinite real-number solutions, and we'd actually have to find an integer solution that minimizes cost)
        // Come to find out, after toiling on this consideration, AoC's actual input does not exercise it.
        // Meaning, the whole "fewest tokens" part of this puzzle is just a time-wasting misdirection. Great.
        //
        // Solved system of equations:
        //
        //  A*a_x + B*b_x = p_x
        //  A*a_y + B*b_y = p_y
        //
        //  b_y(A*a_x + B*b_x = p_x)
        // -b_x(A*a_y + B*b_y = p_y)
        //
        //  A*a_x*b_y =  p_x*b_y
        // -A*a_y*b_x = -p_y*b_x
        //
        //  A = (p_x*b_y - p_y*b_x)/(a_x*b_y - a_y*b_x)
        //  B = (p_x - A*a_x)/b_x
        
        long nA = (p.x*b.y - p.y*b.x)/(a.x*b.y - a.y*b.x),
             nB = (p.x - nA*a.x)/b.x;
        // Check that solutions work (ie integer divisions had no remainder)
        if (nA*a.x + nB*b.x != p.x || nA*a.y + nB*b.y != p.y) {
            return 0;
        }
        return 3*nA + nB;
    }
}
