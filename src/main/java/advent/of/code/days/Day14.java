package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 implements Day {
    static final Pattern PATTERN = Pattern.compile(".+?([+-]?\\d+)".repeat(4));
    
    @Override
    public void part1(IO io) {
        int leny = 103, lenx = 101, midy = leny/2, midx = lenx/2;
        long[] quadrants = new long[4];
        for (Scanner s = io.scanner(); s.hasNextLine(); ) {
            Robot r = parseRobot(s.nextLine());
            int xf = Math.floorMod(r.x + 100*r.dx, lenx),
                yf = Math.floorMod(r.y + 100*r.dy, leny);
            if (xf < midx && yf < midy) { quadrants[0]++; }
            else if (xf >= lenx-midx && yf < midy) { quadrants[1]++; }
            else if (xf < midx && yf >= leny-midy) { quadrants[2]++; }
            else if (xf >= lenx-midx && yf >= leny-midy) { quadrants[3]++; }
        }
        long product = Arrays.stream(quadrants).reduce(1L, (a,b) -> a*b);
        io.out().println(product);
    }
    
    @Override
    public void part2(IO io) {
        int leny = 103, lenx = 101;
        Robot[] robots = io.lines().map(this::parseRobot).toArray(Robot[]::new);
        int[][] grid = new int[leny][lenx];
        for (Robot r : robots) {
            grid[r.y][r.x]++;
        }
        for (int i = 0; i < 1000000; i++) {
            // Look for a majority of robots in the center
            int count = 0, threshold = (int) (robots.length * 0.7);
            for (Robot r : robots) {
                if (lenx/4 < r.x && r.x <= 3*lenx/4 && leny/4 < r.y && r.y <= 3*leny/4) {
                    count++;
                }
            }
            if (count > threshold) {
                io.out().println(i);
//                for (var row : grid) {
//                    io.out().println(Arrays.stream(row).mapToObj(n -> n == 0 ? "  " : "██").collect(Collectors.joining()));
//                }
                return;
            }
            for (Robot r : robots) {
                grid[r.y][r.x]--;
                r.x = Math.floorMod(r.x + r.dx, lenx);
                r.y = Math.floorMod(r.y + r.dy, leny);
                grid[r.y][r.x]++;
            }
        }
        throw new IllegalStateException("No Christmas tree!");
    }
    
    static class Robot {
        int x, y, dx, dy;
        Robot(int x, int y, int dx, int dy) { this.x = x; this.y = y; this.dx = dx; this.dy = dy; }
    }
    
    Robot parseRobot(String line) {
        Matcher m = PATTERN.matcher(line);
        if (m.find()) {
            return new Robot(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)),
                             Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
        }
        throw new IllegalStateException();
    }
}
