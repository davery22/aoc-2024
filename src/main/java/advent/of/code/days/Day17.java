package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 implements Day {
    static final Pattern REGISTER_PATTERN = Pattern.compile("Register [A|B|C]: ([+-]?\\d+)");
    static final Pattern PROGRAM_PATTERN = Pattern.compile("Program: ([0-7](?:,[0-7])+)");
    
    @Override
    public void part1(IO io) {
        long[] reg = new long[3];
        Scanner s = io.scanner();
        for (int i = 0; i < 3; i++) {
            Matcher m = REGISTER_PATTERN.matcher(s.nextLine()); m.find();
            reg[i] = Long.parseLong(m.group(1));
        }
        s.nextLine(); // Skip blank line
        Matcher m = PROGRAM_PATTERN.matcher(s.nextLine()); m.find();
        int[] program = Arrays.stream(m.group(1).split(",")).mapToInt(Integer::parseInt).toArray();
        StringBuilder sb = new StringBuilder();
        Consumer<Long> out = i -> sb.append(sb.isEmpty() ? "" : ",").append(i);
        run(program, reg, out, false);
        io.out().println(sb);
    }
    
    @Override
    public void part2(IO io) {
        long[] reg = new long[4]; // Extra space for a tmp
        Scanner s = io.scanner();
        for (int i = 0; i < 3; i++) {
            Matcher m = REGISTER_PATTERN.matcher(s.nextLine()); m.find();
            reg[i] = Long.parseLong(m.group(1));
        }
        s.nextLine(); // Skip blank line
        Matcher m = PROGRAM_PATTERN.matcher(s.nextLine()); m.find();
        int[] program = Arrays.stream(m.group(1).split(",")).mapToInt(Integer::parseInt).toArray();
        reg[0] = 0;
        dfs(reg, program, 0);
        io.out().println(reg[0]);
    }
    
    boolean dfs(long[] reg, int[] program, int i) {
        if (i == program.length) {
            return true;
        }
        // Build up A by recursively finding the next low 3 bits that cause the output to match the next program value
        long a = reg[0], b = reg[1], c = reg[2];
        for (long lo = 0; lo < 8; lo++) {
            reg[0] = a*8+lo; reg[1] = b; reg[2] = c;
            run(program, reg, v -> reg[3] = v, true);
            if (reg[3] == program[program.length-i-1]) {
                reg[0] = a*8+lo; reg[1] = b; reg[2] = c;
                if (dfs(reg, program, i+1)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    void run(int[] program, long[] reg, Consumer<Long> out, boolean jumpOut) {
        for (int i = 0; i < program.length; i += 2) {
            int operand = program[i+1];
            switch (program[i]) {
                case 0 -> reg[0] = reg[0] / (1L << combo(operand, reg));
                case 1 -> reg[1] = reg[1] ^ operand;
                case 2 -> reg[1] = combo(operand, reg) & 7;
                case 3 -> i = jumpOut ? program.length : (reg[0] == 0 ? i : operand-2);
                case 4 -> reg[1] = reg[1] ^ reg[2];
                case 5 -> out.accept(combo(operand, reg) & 7);
                case 6 -> reg[1] = reg[0] / (1L << combo(operand, reg));
                case 7 -> reg[2] = reg[0] / (1L << combo(operand, reg));
            }
        }
    }
    
    long combo(int operand, long[] reg) {
        return operand < 4 ? operand : reg[operand & 3];
    }
}
