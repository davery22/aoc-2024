package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Day22 implements Day {
    @Override
    public void part1(IO io) {
        long mod = 16777216;
        long sum = io.lines()
            .mapToLong(line -> {
                long secret = Long.parseLong(line);
                for (int i = 0; i < 2000; i++) {
                    secret = ((secret * 64) ^ secret) % mod;
                    secret = ((secret / 32) ^ secret) % mod;
                    secret = ((secret * 2048) ^ secret) % mod;
                }
                return secret;
            })
            .sum();
        io.out().println(sum);
    }
    
    @Override
    public void part2(IO io) {
        long mod = 16777216;
        Map<List<Integer>, Integer> bananasBySequence = new ConcurrentHashMap<>();
        io.lines().parallel().forEach(line -> {
            long secret = Long.parseLong(line);
            Set<List<Integer>> seen = new HashSet<>();
            int[] prices = new int[2001];
            List<Integer> changes = new ArrayList<>(2001);
            prices[0] = (int) (secret % 10);
            changes.add(0);
            for (int i = 1; i < 2001; i++) {
                secret = ((secret * 64) ^ secret) % mod;
                secret = ((secret / 32) ^ secret) % mod;
                secret = ((secret * 2048) ^ secret) % mod;
                int price = (int) (secret % 10);
                changes.add((prices[i] = price) - prices[i-1]);
            }
            for (int i = 1; i+3 < 2001; i++) {
                List<Integer> sequence = changes.subList(i, i+4);
                if (seen.add(sequence)) {
                    bananasBySequence.merge(sequence, prices[i+3], Integer::sum);
                }
            }
        });
        int max = bananasBySequence.values().stream().mapToInt(i -> i).max().orElseThrow();
        io.out().println(max);
    }
}
