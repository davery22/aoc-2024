package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.Arrays;

public class Day21 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, 2);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, 25);
    }
    
    // Coordinates for buttons on the numeric keypad, in order 0-9A
    static final int[] NPAD_X = new int[]{ 1, 0, 1, 2, 0, 1, 2, 0, 1, 2, 2 };
    static final int[] NPAD_Y = new int[]{ 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 3 };
    
    // Coordinates for buttons on the directional keypad, in order <v>^A
    static final int[] DPAD_X = new int[]{ 0, 1, 2, 1, 2 };
    static final int[] DPAD_Y = new int[]{ 1, 1, 1, 0, 0 };
    
    void solve(IO io, int layers) {
        // Use DP to tabulate the top-level cost of every path between bottom-level directional keypad buttons.
        long[][] tab1 = new long[5][5];
        long[][] tab2 = new long[5][5];
        for (int i = 0; i < 5; i++) {
            Arrays.fill(tab1[i], 1);
        }
        for (int i = 0; i < layers; i++) {
            for (int row = 0; row < 5; row++) {
                int y = DPAD_Y[row], x = DPAD_X[row];
                for (int col = 0; col < 5; col++) {
                    tab2[row][col] = 0;
                    int curr = 4, d1, d2, s1, s2, ny = DPAD_Y[col], nx = DPAD_X[col], v = ny > y ? 1 : 3, h = nx > x ? 2 : 0;
                    // To minimize higher-level costs, prefer < first and > last, as long as we don't cross the gap
                    if ((y == 0 && nx == 0) || nx > x && !(x == 0 && ny == 0)) {
                        d1 = Math.abs(ny-y); d2 = Math.abs(nx-x); s1 = v; s2 = h;
                    } else {
                        d1 = Math.abs(nx-x); d2 = Math.abs(ny-y); s1 = h; s2 = v;
                    }
                    for (int j = 0; j < d1; j++) {
                        tab2[row][col] += tab1[curr][curr = s1];
                    }
                    for (int j = 0; j < d2; j++) {
                        tab2[row][col] += tab1[curr][curr = s2];
                    }
                    tab2[row][col] += tab1[curr][4];
                }
            }
            long[][] tmp = tab1; tab1 = tab2; tab2 = tmp;
        }
        
        // Sum the top-level costs of the actual paths between bottom-level numeric keypad buttons.
        long[][] tab = tab1;
        long sum = io.lines()
            .mapToLong(line -> {
                long steps = 0;
                int number = Integer.parseInt(line.substring(0, line.length()-1));
                int curr = 4, y = NPAD_Y[10], x = NPAD_X[10];
                for (int i = 0; i < line.length(); i++) {
                    int id = Character.getNumericValue(line.charAt(i));
                    int d1, d2, s1, s2, ny = NPAD_Y[id], nx = NPAD_X[id], v = ny > y ? 1 : 3, h = nx > x ? 2 : 0;
                    // To minimize higher-level costs, prefer < first and > last, as long as we don't cross the gap
                    if ((y == 3 && nx == 0) || nx > x && !(x == 0 && ny == 3)) {
                        d1 = Math.abs(ny-y); d2 = Math.abs(nx-x); s1 = v; s2 = h;
                    } else {
                        d1 = Math.abs(nx-x); d2 = Math.abs(ny-y); s1 = h; s2 = v;
                    }
                    for (int j = 0; j < d1; j++) {
                        steps += tab[curr][curr = s1];
                    }
                    for (int j = 0; j < d2; j++) {
                        steps += tab[curr][curr = s2];
                    }
                    steps += tab[curr][curr = 4];
                    y = ny; x = nx;
                }
                return steps * number;
            })
            .sum();
        
        io.out().println(sum);
    }
}
