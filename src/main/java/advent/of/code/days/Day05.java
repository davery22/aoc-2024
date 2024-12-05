package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day05 implements Day {
    @Override
    public void part1(IO io) {
        solve(io, true);
    }
    
    @Override
    public void part2(IO io) {
        solve(io, false);
    }
    
    private void solve(IO io, boolean useCorrectlyOrdered) {
        Scanner s = io.scanner().useDelimiter(IO.EOL);
        Map<Integer, List<Integer>> orderingRules = s.tokens()
            .takeWhile(line -> !line.isBlank())
            .map(line -> line.split("\\|"))
            .collect(Collectors.groupingBy(
                parts -> Integer.parseInt(parts[0]),
                Collectors.mapping(parts -> Integer.parseInt(parts[1]), Collectors.toList()))
            );
        int sum = s.tokens()
            .map(line -> Stream.of(line.split(",")).mapToInt(Integer::parseInt).toArray())
            .filter(update -> useCorrectlyOrdered == isOrderedCorrectly(update, orderingRules))
            .map(update -> useCorrectlyOrdered ? update :
                 // No way to sort int[] by a custom Comparator, so need to copy :/
                 Arrays.stream(update).boxed()
                     .sorted((a, b) ->
                           orderingRules.getOrDefault(a, List.of()).contains(b) ? -1
                         : orderingRules.getOrDefault(b, List.of()).contains(a) ?  1
                         : 0
                     )
                     .mapToInt(i -> i)
                     .toArray()
            )
            .mapToInt(update -> update[update.length/2])
            .sum();
        io.out().println(sum);
    }
    
    boolean isOrderedCorrectly(int[] update, Map<Integer, List<Integer>> orderingRules) {
        Map<Integer, Integer> indexByPageNo = IntStream.range(0, update.length).boxed().collect(Collectors.toMap(i -> update[i], i -> i));
        for (int i = 0; i < update.length; i++) {
            int pageNo = update[i];
            for (int oPageNo : orderingRules.getOrDefault(pageNo, List.of())) {
                Integer oi = indexByPageNo.get(oPageNo);
                if (oi != null && oi <= i) {
                    return false;
                }
            }
        }
        return true;
    }
}
