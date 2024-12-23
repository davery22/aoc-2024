package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 implements Day {
    @Override
    public void part1(IO io) {
        Map<String, Set<String>> graph = new HashMap<>();
        io.lines().forEach(line -> {
            String[] parts = line.split("-");
            graph.computeIfAbsent(parts[0], _ -> new HashSet<>()).add(parts[1]);
            graph.computeIfAbsent(parts[1], _ -> new HashSet<>()).add(parts[0]);
        });
        Set<Set<String>> cliques = new HashSet<>();
        graph.forEach((node1, neighbors) -> {
            if (!node1.startsWith("t")) {
                return;
            }
            for (String node2 : neighbors) {
                for (String node3 : graph.get(node2)) {
                    if (!node1.equals(node3) && neighbors.contains(node3)) {
                        cliques.add(Set.of(node1, node2, node3));
                    }
                }
            }
        });
        io.out().println(cliques.size());
    }
    
    @Override
    public void part2(IO io) {
        Map<String, Set<String>> graph = new HashMap<>();
        io.lines().forEach(line -> {
            String[] parts = line.split("-");
            graph.computeIfAbsent(parts[0], _ -> new HashSet<>()).add(parts[1]);
            graph.computeIfAbsent(parts[1], _ -> new HashSet<>()).add(parts[0]);
        });
        List<Set<String>> cliques = new ArrayList<>();
        findMaximalCliques(new HashSet<>(), new HashSet<>(graph.keySet()), new HashSet<>(), graph, cliques);
        String maxClique = cliques.stream()
            .max(Comparator.comparingInt(Set::size)).orElseThrow().stream()
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.joining(","));
        io.out().println(maxClique);
    }
    
    // Bron-Kerbosch
    void findMaximalCliques(Set<String> r, Set<String> p, Set<String> x,
                            Map<String, Set<String>> graph, List<Set<String>> cliques) {
        if (p.isEmpty() && x.isEmpty()) {
            cliques.add(new HashSet<>(r));
        }
        for (var iter = p.iterator(); iter.hasNext(); ) {
            var v = iter.next();
            boolean added = r.add(v);                                      // R ⋃ {v}
            Set<String> np = new HashSet<>(p); np.retainAll(graph.get(v)); // P ⋂ N(v)
            Set<String> nx = new HashSet<>(x); nx.retainAll(graph.get(v)); // X ⋂ N(v)
            findMaximalCliques(r, np, nx, graph, cliques);
            if (added) r.remove(v);
            iter.remove();                                                 // P = P \ {v}
            x.add(v);                                                      // X = X ⋃ {v}
        }
    }
}
