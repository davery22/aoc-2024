package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.ArrayList;
import java.util.List;

public class Day20 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, 100, 2);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, 100, 20);
    }
    
    void solve(IO io, int minSavings, int maxCost) {
        int[][] grid = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int y = 0, x = 0, leny = grid.length, lenx = grid[0].length;
        for (int i = 0; i < leny; i++) {
            for (int j = 0; j < lenx; j++) {
                if (grid[i][j] == 'S') {
                    y = i; x = j;
                    break;
                }
            }
        }
        // Find all positions in the path, in order
        record Pos(int y, int x) {}
        List<Pos> posns = new ArrayList<>(List.of(new Pos(y, x)));
        grid[y][x] = '#';
        loop: for (;;) {
            for (int dy = -1, dx = 0, i = 0; i < 4; i++) {
                int tmp = dy; dy = dx; dx = -tmp;
                int ny = y+dy, nx = x+dx;
                if (ny >= 0 && ny < leny && nx >= 0 && nx < lenx && grid[ny][nx] != '#') {
                    grid[y = ny][x = nx] = '#';
                    posns.add(new Pos(y, x));
                    continue loop;
                }
            }
            break;
        }
        // For positions at least minSavings steps apart, see if traveling directly
        // (Manhattan distance) is within maxCost and preserves at least minSavings.
        int count = 0;
        for (int i = 0; i < posns.size()-minSavings; i++) {
            Pos a = posns.get(i);
            for (int j = i + minSavings; j < posns.size(); j++) {
                Pos b = posns.get(j);
                int cost = Math.abs(b.y-a.y) + Math.abs(b.x-a.x);
                if (cost <= maxCost && (j-i-cost) >= minSavings) {
                    count++;
                }
            }
        }
        io.out().println(count);
    }
}
