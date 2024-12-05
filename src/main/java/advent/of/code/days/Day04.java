package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

public class Day04 implements Day {
    @Override
    public void part1(IO io) {
        int[][] grid = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int leny = grid.length, lenx = grid[0].length, count = 0;
        for (int y = 0; y < leny; y++) {
            for (int x = 0; x < lenx; x++) {
                // We could just spell out the eight possible patterns.
                // Instead, we'll use iteration to tighten up the checks.
                if ('X' == grid[y][x]) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (!(dy == 0 && dx == 0) && check(grid, y, x, dy, dx)) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        io.out().println(count);
    }
    
    boolean check(int[][] grid, int y0, int x0, int dy, int dx) {
        String target = "MAS";
        int yend = y0 + dy * target.length(), xend = x0 + dx * target.length();
        if (yend < 0 || yend >= grid.length || xend < 0 || xend >= grid[0].length) {
            return false;
        }
        for (int i = 0; i < target.length(); i++) {
            if (grid[y0 + dy * (i+1)][x0 + dx * (i+1)] != target.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void part2(IO io) {
        int[][] grid = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int leny = grid.length, lenx = grid[0].length, count = 0;
        for (int y = 1; y < leny-1; y++) {
            for (int x = 1; x < lenx-1; x++) {
                // We could just spell out the four possible patterns.
                // Instead, we'll check that the letters on a diagonal are different,
                // and then use a trick to check for two M's and two S's in the corners.
                if ('A' == grid[y][x]
                    && grid[y-1][x-1] != grid[y+1][x+1]
                    && 8 == score(grid[y-1][x-1])
                          + score(grid[y-1][x+1])
                          + score(grid[y+1][x-1])
                          + score(grid[y+1][x+1])
                ) {
                    count++;
                }
            }
        }
        io.out().println(count);
    }
    
    int score(int ch) {
        return 'M' == ch ? 3 : 'S' == ch ? 1 : 0;
    }
}
