package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.HashSet;
import java.util.Set;

public class Day10 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, this::score);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, this::rating);
    }
    
    void solve(IO io, Metric metric) {
        int[][] grid = io.lines().map(line -> line.chars().map(Character::getNumericValue).toArray()).toArray(int[][]::new);
        int sum = 0, leny = grid.length, lenx = grid[0].length;
        for (int y = 0; y < leny; y++) {
            for (int x = 0; x < lenx; x++) {
                if (grid[y][x] == 0) {
                    sum += metric.measure(grid, y, x);
                }
            }
        }
        io.out().println(sum);
    }
    
    record Pos(int y, int x) {}
    interface Metric {
        int measure(int[][] grid, int y0, int x0);
    }
    
    int score(int[][] grid, int y, int x) {
        Set<Pos> summits = new HashSet<>();
        score(summits, grid, y, x);
        return summits.size();
    }
    
    void score(Set<Pos> summits, int[][] grid, int y, int x) {
        if (grid[y][x] == 9) {
            summits.add(new Pos(y, x));
        }
        int leny = grid.length, lenx = grid[0].length;
        for (int dy = -1, dx = 0, i = 0; i < 4; i++) {
            int tmp = dy; dy = dx; dx = -tmp;
            int ny = y+dy, nx = x+dx;
            if (ny >= 0 && ny < leny && nx >= 0 && nx < lenx && grid[ny][nx] == grid[y][x]+1) {
                score(summits, grid, ny, nx);
            }
        }
    }
    
    int rating(int[][] grid, int y, int x) {
        if (grid[y][x] == 9) {
            return 1;
        }
        int sum = 0, leny = grid.length, lenx = grid[0].length;
        for (int dy = -1, dx = 0, i = 0; i < 4; i++) {
            int tmp = dy; dy = dx; dx = -tmp;
            int ny = y+dy, nx = x+dx;
            if (ny >= 0 && ny < leny && nx >= 0 && nx < lenx && grid[ny][nx] == grid[y][x]+1) {
                sum += rating(grid, ny, nx);
            }
        }
        return sum;
    }
}
