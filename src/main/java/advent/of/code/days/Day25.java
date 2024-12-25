package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day25 implements Day {
    @Override
    public void part1(IO io) {
        List<int[]> locks = new ArrayList<>(), keys = new ArrayList<>();
        Scanner s = io.scanner().useDelimiter(IO.EOL);
        while (s.hasNextLine()) {
            String line = s.nextLine();
            int[] pins = new int[5];
            int ch;
            if (line.equals("#####")) {
                locks.add(pins);
                ch = '#';
            } else {
                keys.add(pins);
                ch = '.';
            }
            while (s.hasNextLine() && !(line = s.nextLine()).isBlank()) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ch) {
                        pins[i]++;
                    }
                }
            }
        }
        int count = 0;
        for (int[] lock : locks) {
            check: for (int[] key : keys) {
                for (int i = 0; i < 5; i++) {
                    if (lock[i] > key[i]) {
                        continue check;
                    }
                }
                count++;
            }
        }
        io.out().println(count);
    }
    
    @Override
    public void part2(IO io) {
        io.out().println("Delivered the Chronicle");
    }
}
