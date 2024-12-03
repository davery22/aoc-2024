package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.regex.Pattern;

public class Day03 implements Day {
    static final Pattern MUL_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    
    @Override
    public void part1(IO io) {
        long sum = io.scanner().findAll(MUL_PATTERN)
            .mapToLong(match -> Long.parseLong(match.group(1)) * Long.parseLong(match.group(2)))
            .sum();
        System.out.println(sum);
    }
    
    @Override
    public void part2(IO io) {
        var state = new Object(){ boolean enabled = true; };
        Pattern instr = Pattern.compile(MUL_PATTERN.pattern() + "|do\\(\\)|don't\\(\\)");
        long sum = io.scanner().findAll(instr)
            .mapToLong(match -> switch (match.group(0)) {
                case "do()" -> { state.enabled = true; yield 0; }
                case "don't()" -> { state.enabled = false; yield 0; }
                default -> state.enabled ? Long.parseLong(match.group(1)) * Long.parseLong(match.group(2)) : 0;
            })
            .sum();
        System.out.println(sum);
    }
}
