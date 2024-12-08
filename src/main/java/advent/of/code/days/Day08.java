package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day08 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, false);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, true);
    }
    
    void solve(IO io, boolean useHarmonics) {
        int[][] grid = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int leny = grid.length, lenx = grid[0].length;
        record Pos(int y, int x) {}
        Map<Integer, List<Pos>> posByFreq = IntStream.range(0, leny).boxed()
            .flatMap(y -> IntStream.range(0, lenx)
                .filter(x -> grid[y][x] != '.')
                .mapToObj(x -> new Pos(y, x))
            )
            .collect(Collectors.groupingBy(p -> grid[p.y][p.x]));
        Set<Pos> antinodes = new HashSet<>();
        for (List<Pos> positions : posByFreq.values()) {
            for (int i = 0; i < positions.size(); i++) {
                Pos p1 = positions.get(i);
                for (int j = i+1; j < positions.size(); j++) {
                    Pos p2 = positions.get(j);
                    for (int k = 0; k < 2; k++) {
                        Pos tmp = p1; p1 = p2; p2 = tmp;
                        if (useHarmonics) {
                            for (int dy = p1.y-p2.y, dx = p1.x-p2.x, ay = p1.y, ax = p1.x;
                                ay >= 0 && ay < leny && ax >= 0 && ax < lenx;
                                ay += dy, ax += dx
                            ) {
                                antinodes.add(new Pos(ay, ax));
                            }
                        } else {
                            int ay = 2*p1.y-p2.y, ax = 2*p1.x-p2.x;
                            if (ay >= 0 && ay < leny && ax >= 0 && ax < lenx) {
                                antinodes.add(new Pos(ay, ax));
                            }
                        }
                    }
                }
            }
        }
        io.out().println(antinodes.size());
    }
}
