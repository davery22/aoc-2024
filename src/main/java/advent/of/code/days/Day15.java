package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.stream.IntStream;

public class Day15 implements Day {
    @Override
    public void part1(IO io) {
        Scanner s = io.scanner().useDelimiter(IO.EOL);
        int[][] grid = s.tokens()
            .takeWhile(line -> !line.isBlank())
            .map(line -> line.chars().toArray())
            .toArray(int[][]::new);
        int[] moves = s.tokens().flatMapToInt(String::chars).toArray(), yx = yx(grid);
        for (int move : moves) {
            int[] dyx = dir(move);
            int ny = yx[0] + dyx[0], nx = yx[1] + dyx[1];
            while (grid[ny][nx] == 'O') {
                ny += dyx[0]; nx += dyx[1];
            }
            if (grid[ny][nx] == '#') {
                continue;
            }
            while (ny != yx[0] || nx != yx[1]) {
                grid[ny][nx] = grid[ny -= dyx[0]][nx -= dyx[1]];
            }
            grid[yx[0]][yx[1]] = '.';
            yx[0] += dyx[0]; yx[1] += dyx[1];
        }
        io.out().println(sum(grid, 'O'));
    }
    
    @Override
    public void part2(IO io) {
        Scanner s = io.scanner().useDelimiter(IO.EOL);
        int[][] grid = s.tokens()
            .takeWhile(line -> !line.isBlank())
            .map(line -> line.chars().flatMap(this::widenTile).toArray())
            .toArray(int[][]::new);
        int[] moves = s.tokens().flatMapToInt(String::chars).toArray(), yx = yx(grid);
        for (int move : moves) {
            int[] dyx = dir(move);
            if (dyx[0] == 0) {
                // Horizontal movement works linearly, like in part 1
                int nx = yx[1] + dyx[1];
                while (grid[yx[0]][nx] == '[' || grid[yx[0]][nx] == ']') {
                    nx += 2*dyx[1];
                }
                if (grid[yx[0]][nx] == '#') {
                    continue;
                }
                while (nx != yx[1]) {
                    grid[yx[0]][nx] = grid[yx[0]][nx -= dyx[1]];
                }
                grid[yx[0]][yx[1]] = '.';
            } else {
                // Vertical movement can fan out - collect all tiles and move them forward if none are obstructed
                Set<Tile> frontier = new HashSet<>();
                if (isObstructed(yx[0], yx[1], dyx[0], grid, frontier)) {
                    continue;
                }
                frontier.forEach(tile -> grid[tile.y][tile.x] = '.');
                frontier.forEach(tile -> grid[tile.y + dyx[0]][tile.x] = tile.tile);
            }
            yx[0] += dyx[0]; yx[1] += dyx[1];
        }
        io.out().println(sum(grid, '['));
    }
    
    static final int[][] DIRS = new int[][]{{-1,0},{0,1},{1,0},{0,-1}};
    int[] dir(int move) {
        return DIRS["^>v<".indexOf(move)];
    }
    
    int[] yx(int[][] grid) {
        return IntStream.range(0, grid.length)
            .mapToObj(y -> IntStream.range(0, grid[0].length).filter(x -> grid[y][x] == '@').mapToObj(x -> new int[]{y,x}))
            .flatMap(s -> s).findFirst().orElseThrow();
    }
    
    int sum(int[][] grid, int ch) {
        return IntStream.range(0, grid.length)
            .flatMap(y -> IntStream.range(0, grid[0].length).filter(x -> grid[y][x] == ch).map(x -> 100*y + x))
            .sum();
    }
    
    IntStream widenTile(int tile) {
        return switch (tile) {
            case 'O' -> IntStream.of('[', ']');
            case '@' -> IntStream.of('@', '.');
            default -> IntStream.of(tile, tile);
        };
    }
    
    record Tile(int y, int x, int tile) {}
    boolean isObstructed(int y, int x, int dy, int[][] grid, Set<Tile> frontier) {
        int tile = grid[y][x];
        return switch (tile) {
            case '#' -> true;
            case '.' -> false;
            case '@' -> frontier.add(new Tile(y, x, tile)) && isObstructed(y+dy, x, dy, grid, frontier);
            case '[',']' -> frontier.add(new Tile(y, x, tile)) && (isObstructed(y+dy, x, dy, grid, frontier) || isObstructed(y, tile == '[' ? x+1 : x-1, dy, grid, frontier));
            default -> throw new IllegalStateException();
        };
    }
}
