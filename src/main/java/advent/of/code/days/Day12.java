package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

public class Day12 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, false);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, true);
    }
    
    void solve(IO io, boolean useSides) {
        int[][] grid = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int sum = 0, id = 1, leny = grid.length, lenx = grid[0].length;
        int[][] visited = new int[leny][lenx];
        for (int y = 0; y < leny; y++) {
            for (int x = 0; x < lenx; x++) {
                if (visited[y][x] == 0) {
                    Region region = new Region(id++, grid[y][x]);
                    explore(region, grid, visited, y, x);
                    sum += region.area * (useSides ? calculateSides(region, visited) : region.perimeter);
                }
            }
        }
        io.out().println(sum);
    }
    
    static class Region {
        int id, type, area, perimeter,
            // Bounding box (for part 2)
            y0 = Integer.MAX_VALUE, x0 = Integer.MAX_VALUE, yN = -1, xN = -1;
        Region(int id, int type) { this.id = id; this.type = type; }
    }
    
    void explore(Region region, int[][] grid, int[][] visited, int y, int x) {
        if (visited[y][x] != 0) {
            return;
        }
        visited[y][x] = region.id;
        region.area++;
        // Adjust bounding box (for part 2)
        region.y0 = Math.min(region.y0, y);
        region.yN = Math.max(region.yN, y+1);
        region.x0 = Math.min(region.x0, x);
        region.xN = Math.max(region.xN, x+1);
        int leny = grid.length, lenx = grid[0].length;
        for (int dy = -1, dx = 0, i = 0; i < 4; i++) {
            int tmp = dy; dy = dx; dx = -tmp;
            int ny = y+dy, nx = x+dx;
            if (ny >= 0 && ny < leny && nx >= 0 && nx < lenx && grid[ny][nx] == region.type) {
                explore(region, grid, visited, ny, nx);
            } else { // Region boundary
                region.perimeter++;
            }
        }
    }
    
    int calculateSides(Region region, int[][] visited) {
        // Loop over the region's bounding box.
        // For each row, take note of where we enter (outside wall) and exit (inside wall) the region.
        // If there is no same-sided wall above, add to sides
        int sides = 0;
        for (int y = region.y0; y < region.yN; y++) {
            boolean inRegion = false;
            for (int x = region.x0; x < region.xN; x++) {
                if ((visited[y][x] == region.id) == inRegion) {
                    continue;
                }
                // Just exited or entered the region - look up
                if (inRegion ? (y == region.y0 || visited[y-1][x] == region.id ||                   visited[y-1][x-1] != region.id )
                             : (y == region.y0 || visited[y-1][x] != region.id || (x > region.x0 && visited[y-1][x-1] == region.id))
                ) {
                    sides++;
                }
                inRegion ^= true; // flip
            }
            // May have just exited the region
            if (inRegion && (y == region.y0 || visited[y-1][region.xN-1] != region.id)) {
                sides++;
            }
        }
        // Vertical sides == horizontal sides
        return sides * 2;
    }
}
