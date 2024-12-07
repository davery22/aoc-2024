package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

public class Day06 implements Day {
    @Override
    public void part1(IO io) {
        int[][] grid = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int y0 = 0, x0 = 0, leny = grid.length, lenx = grid[0].length;
        for (int y = 0; y < leny; y++) {
            for (int x = 0; x < lenx; x++) {
                if (grid[y][x] == '^') {
                    y0 = y;
                    x0 = x;
                    break;
                }
            }
        }
        grid[y0][x0] = 'X';
        int steps = 1, y = y0, x = x0, dy = -1, dx = 0;
        outer: for (;;) {
            for (;;) {
                int ny = y+dy, nx = x+dx;
                if (ny < 0 || ny >= leny || nx < 0 || nx >= lenx) {
                    break outer;
                }
                if (grid[ny][nx] != '#') {
                    break;
                }
                // Rotate right (-1,0 -> 0,1 -> 1,0 -> 0,-1)
                int tmp = dy;
                dy = dx;
                dx = -tmp;
            }
            y += dy;
            x += dx;
            if (grid[y][x] != 'X') {
                grid[y][x] = 'X';
                steps++;
            }
        }
        io.out().println(steps);
    }
    
    @Override
    public void part2(IO io) {
        int[][] grid = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int y0 = 0, x0 = 0, leny = grid.length, lenx = grid[0].length;
        for (int y = 0; y < leny; y++) {
            for (int x = 0; x < lenx; x++) {
                if (grid[y][x] == '^') {
                    y0 = y;
                    x0 = x;
                }
                if (grid[y][x] != '#') {
                    grid[y][x] = 0;
                }
            }
        }
        int choices = 0, dir = 1, y = y0, x = x0, dy = -1, dx = 0;
        outer: for (;;) {
            grid[y][x] |= dir;
            for (;;) {
                int ny = y+dy, nx = x+dx;
                if (ny < 0 || ny >= leny || nx < 0 || nx >= lenx) {
                    break outer;
                }
                if (grid[ny][nx] != '#') {
                    break;
                }
                // Rotate right (-1,0 -> 0,1 -> 1,0 -> 0,-1)
                int tmp = dy;
                dy = dx;
                dx = -tmp;
                dir = (dir == 0b1000) ? 1 : (dir << 1); // circular shift on low 4 bits
            }
            y += dy;
            x += dx;
            // If we haven't stepped here before, try introducing an obstacle to cause a cycle
            if (grid[y][x] == 0) {
                grid[y][x] = '#';
                int steps = stepsToCycle(dir, y-dy, x-dx, dy, dx, grid);
                if (steps > 0) {
                    choices++;
                }
                eraseSteps(Math.abs(steps), dir, y-dy, x-dx, dy, dx, grid);
                grid[y][x] = 0;
            }
        }
        io.out().println(choices);
    }
    
    int stepsToCycle(int dir, int y, int x, int dy, int dx, int[][] grid) {
        int leny = grid.length, lenx = grid[0].length;
        // Advance simulation until we find a cycle or exit the grid
        for (int steps = 1;; steps++) {
            for (;;) {
                int ny = y+dy, nx = x+dx;
                if (ny < 0 || ny >= leny || nx < 0 || nx >= lenx) {
                    return -steps;
                }
                if (grid[ny][nx] != '#') {
                    break;
                }
                // Rotate right (-1,0 -> 0,1 -> 1,0 -> 0,-1)
                int tmp = dy;
                dy = dx;
                dx = -tmp;
                dir = (dir == 0b1000) ? 1 : (dir << 1); // circular shift on low 4 bits
            }
            y += dy;
            x += dx;
            if (grid[y][x] == (grid[y][x] |= dir)) {
                return steps;
            }
        }
    }
    
    void eraseSteps(int steps, int dir, int y, int x, int dy, int dx, int[][] grid) {
        while (--steps > 0) {
            for (;;) {
                int ny = y+dy, nx = x+dx;
                if (grid[ny][nx] != '#') {
                    break;
                }
                // Rotate right (-1,0 -> 0,1 -> 1,0 -> 0,-1)
                int tmp = dy;
                dy = dx;
                dx = -tmp;
                dir = (dir == 0b1000) ? 1 : (dir << 1); // circular shift on low 4 bits
            }
            y += dy;
            x += dx;
            grid[y][x] &= ~dir;
        }
    }
}
