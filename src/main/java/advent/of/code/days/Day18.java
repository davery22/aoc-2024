package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.IntStream;

public class Day18 implements Day {
    @Override
    public void part1(IO io) {
        int len = 71, steps = 1024;
        int[][] grid = IntStream.range(0, len).mapToObj(_ -> IntStream.range(0, len).map(_ -> Integer.MAX_VALUE).toArray()).toArray(int[][]::new);
        Pos[] bytes = parseFallingBytes(io);
        for (int i = 0; i < steps; i++) {
            Pos p = bytes[i];
            grid[p.y][p.x] = -1;
        }
        Pos start = new Pos(0, 0), end = new Pos(len-1, len-1);
        shortestPath(grid, start, end);
        io.out().println(grid[end.y][end.x]);
    }
    
    @Override
    public void part2(IO io) {
        int len = 71, steps = 1024;
        int[][] grid0 = IntStream.range(0, len).mapToObj(_ -> IntStream.range(0, len).map(_ -> Integer.MAX_VALUE).toArray()).toArray(int[][]::new);
        Pos[] bytes = parseFallingBytes(io);
        for (int i = 0; i < steps; i++) { // We know from part 1 the exit is still reachable after this many steps
            Pos p = bytes[i];
            grid0[p.y][p.x] = -1;
        }
        Pos start = new Pos(0, 0), end = new Pos(len-1, len-1);
        // Binary search for the number of steps that makes the exit unreachable
        int lo = steps, hi = bytes.length;
        while (lo != hi) {
            int mid = (lo+hi)/2;
            // Reset the grid and apply falling bytes
            int[][] grid = Arrays.stream(grid0).map(int[]::clone).toArray(int[][]::new);
            for (int j = steps; j <= mid; j++) {
                Pos p = bytes[j];
                grid[p.y][p.x] = -1;
            }
            shortestPath(grid, start, end);
            if (grid[end.y][end.x] == Integer.MAX_VALUE) {
                hi = mid;
            } else {
                lo = mid+1;
            }
        }
        Pos p = bytes[lo];
        io.out().println(p.x + "," + p.y);
    }
    
    record Pos(int y, int x) {}
    
    Pos[] parseFallingBytes(IO io) {
        return io.lines()
            .map(line -> Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray())
            .map(arr -> new Pos(arr[1], arr[0]))
            .toArray(Pos[]::new);
    }
    
    void shortestPath(int[][] grid, Pos start, Pos end) {
        int len = grid.length;
        Deque<Pos> queue = new ArrayDeque<>();
        queue.offer(start);
        grid[start.y][start.x] = 0;
        for (Pos p; (p = queue.poll()) != null && !p.equals(end); ) {
            int weight = grid[p.y][p.x];
            for (int dy = -1, dx = 0, i = 0; i < 4; i++) {
                int tmp = dy; dy = dx; dx = -tmp;
                int ny = p.y + dy, nx = p.x + dx;
                if (ny >= 0 && ny < len && nx >= 0 && nx < len && grid[ny][nx] > weight+1) {
                    grid[ny][nx] = weight+1;
                    queue.offer(new Pos(ny, nx));
                }
            }
        }
    }
}
