package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.stream.Collectors;

public class Day24 implements Day {
    @Override
    public void part1(IO io) {
        Map<String, Wire> outIn = new HashMap<>();
        Scanner s = io.scanner().useDelimiter(IO.EOL);
        s.tokens().takeWhile(line -> !line.isBlank()).forEach(line -> {
            String[] p = line.split(": ");
            outIn.put(p[0], new Fixed("1".equals(p[1])));
        });
        s.tokens().forEach(line -> {
            String[] p = line.split(" ");
            outIn.put(p[4], new Op(p[0], p[2], Gate.valueOf(p[1])));
        });
        long number = outIn.keySet().stream()
            .filter(out -> out.startsWith("z"))
            .sorted(Comparator.reverseOrder())
            .mapToLong(out -> valueOf(out, outIn) ? 1 : 0)
            .reduce(0, (n, i) -> n*2 + i);
        io.out().println(number);
    }
    
    @Override
    public void part2(IO io) {
        // Looks like a ripple carry adder
        //
        // x_0        XOR y_0        -> z_0
        // x_0        AND y_0        -> carry_0
        // ...
        // x_i        XOR y_i        -> bit_i
        // bit_i      XOR carry_i-1  -> z_i
        // x_i        AND y_i        -> carry_ia
        // bit_i      AND carry_i-1  -> carry_ib
        // carry_ia    OR carry_ib   -> carry_i
        // ...
        // carry_N-1a  OR carry_N-1b -> z_N
        //
        // From this we can derive constraints to detect misplaced outputs
        
        Map<String, Wire> outIn = new HashMap<>();
        Map<String, List<String>> inOut = new HashMap<>();
        Scanner s = io.scanner().useDelimiter(IO.EOL);
        s.tokens().takeWhile(line -> !line.isBlank()).forEach(line -> {
            String[] p = line.split(": ");
            outIn.put(p[0], new Fixed("1".equals(p[1])));
        });
        s.tokens().forEach(line -> {
            String[] p = line.split(" ");
            outIn.put(p[4], new Op(p[0], p[2], Gate.valueOf(p[1])));
            inOut.computeIfAbsent(p[0], _ -> new ArrayList<>()).add(p[4]);
            inOut.computeIfAbsent(p[2], _ -> new ArrayList<>()).add(p[4]);
        });
        String zMax = outIn.keySet().stream().filter(out -> out.startsWith("z")).max(Comparator.naturalOrder()).orElseThrow();
        String ans = outIn.keySet().stream()
            .filter(out -> {
                if (!(outIn.get(out) instanceof Op op)) {
                    return false;
                }
                boolean anyFaults = false;
                // 1. If output is z__ (except zMax), input MUST be an XOR
                anyFaults |= Gate.XOR != op.gate && out.startsWith("z") && !zMax.equals(out);
                // 2. If input is not x__, y__ and output is not z__, input MUST NOT be an XOR
                anyFaults |= Gate.XOR == op.gate && !op.a.startsWith("x") && !op.b.startsWith("y") && !out.startsWith("z");
                if (!op.a.equals("x00") && !op.b.equals("y00")) {
                    // 3. If input is (x__ XOR y__), output MUST be input to another XOR
                    anyFaults |= Gate.XOR == op.gate && op.a.startsWith("x") && op.b.startsWith("y")
                        && inOut.getOrDefault(out, List.of()).stream().map(outIn::get).noneMatch(w -> w instanceof Op o && Gate.XOR == o.gate);
                    // 4. If input is an AND, output MUST be input to another OR
                    anyFaults |= Gate.AND == op.gate
                        && inOut.getOrDefault(out, List.of()).stream().map(outIn::get).noneMatch(w -> w instanceof Op o && Gate.OR == o.gate);
                }
                return anyFaults;
            })
            .sorted()
            .collect(Collectors.joining(","));
        io.out().println(ans);
    }
    
    enum Gate { AND, OR, XOR }
    sealed interface Wire {}
    record Op(String a, String b, Gate gate) implements Wire {
        Op {
            if (a.compareTo(b) > 0) {
                String tmp = a; a = b; b = tmp;
            }
        }
    }
    record Fixed(boolean value) implements Wire {}
    
    boolean valueOf(String out, Map<String, Wire> outIn) {
        return switch (outIn.get(out)) {
            case Fixed f -> f.value;
            case Op op -> {
                boolean a = valueOf(op.a, outIn),
                    b = valueOf(op.b, outIn),
                    v = switch (op.gate) {
                        case AND -> a && b;
                        case OR  -> a || b;
                        case XOR -> a ^ b;
                    };
                outIn.put(out, new Fixed(v));
                yield v;
            }
        };
    }
}
