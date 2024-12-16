package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.*;
import java.util.stream.IntStream;

public class Day16 implements Day {
    @Override
    public void part1(IO io) {
        int[][] maze = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int[] start = find(maze, 'S'), end = find(maze, 'E');
        var scores = getScores(maze, start, end);
        int bestScore = Arrays.stream(DIRS).map(dir -> scores.get(new Step(end[0], end[1], dir[0], dir[1])))
            .min(Comparator.nullsLast(Comparator.naturalOrder()))
            .orElseThrow();
        io.out().println(bestScore);
    }
    
    @Override
    public void part2(IO io) {
        int[][] maze = io.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int[] start = find(maze, 'S'), end = find(maze, 'E');
        var scores = getScores(maze, start, end);
        int bestScore = Arrays.stream(DIRS).map(dir -> scores.get(new Step(end[0], end[1], dir[0], dir[1])))
            .min(Comparator.nullsLast(Comparator.naturalOrder()))
            .orElseThrow();
        // Backtrack from the ending through each tile we could have come from, based on its score
        ArrayDeque<ScoredStep> queue = new ArrayDeque<>();
        for (int[] dir : DIRS) {
            Step s = new Step(end[0], end[1], dir[0], dir[1]);
            if (Objects.equals(bestScore, scores.get(s))) {
                queue.offer(new ScoredStep(s, bestScore));
            }
        }
        Set<Step> visited = new HashSet<>();
        for (ScoredStep ss; (ss = queue.poll()) != null; ) {
            Step s = ss.step;
            if (!visited.add(s)) {
                continue;
            }
            for (int[] to : DIRS) {
                int ny = s.y + to[0], nx = s.x + to[1];
                for (int[] from : DIRS) {
                    Step next = new Step(ny, nx, from[0], from[1]);
                    int expectedScore = (from[0] == s.dy && from[1] == s.dx) ? ss.score-1 : ss.score-1001;
                    if (Objects.equals(expectedScore, scores.get(next))) {
                        queue.add(new ScoredStep(next, expectedScore));
                    }
                }
            }
        }
        // Avoid double-counting same tile from different directions
        long count = visited.stream().map(p -> new Step(p.y, p.x, 0, 0)).distinct().count();
        io.out().println(count);
    }
    
    static final int[][] DIRS = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
    
    record Step(int y, int x, int dy, int dx) {}
    record ScoredStep(Step step, int score) {}
    
    int[] find(int[][] maze, int tile) {
        return IntStream.range(0, maze.length)
            .mapToObj(y -> IntStream.range(0, maze[0].length).filter(x -> maze[y][x] == tile).mapToObj(x -> new int[]{y,x}))
            .flatMap(s -> s).findFirst().orElseThrow();
    }
    
    Map<Step, Integer> getScores(int[][] maze, int[] start, int[] end) {
        Map<Step, Integer> scores = new HashMap<>();
        PriorityQueue<ScoredStep> queue = new PriorityQueue<>(Comparator.comparingInt(ScoredStep::score));
        queue.offer(new ScoredStep(new Step(start[0], start[1], 0, 1), 0));
        int bestScore = Integer.MAX_VALUE, leny = maze.length, lenx = maze[0].length;
        for (ScoredStep ss; (ss = queue.poll()) != null; ) {
            Step s = ss.step;
            if (scores.getOrDefault(s, Integer.MAX_VALUE) <= ss.score) {
                continue;
            }
            scores.put(s, ss.score);
            if (s.y == end[0] && s.x == end[1]) {
                bestScore = Math.min(bestScore, ss.score);
                continue;
            }
            for (int[] dir : DIRS) {
                if (dir[0] == -s.dy && dir[1] == -s.dx) {
                    continue;
                }
                int ny = s.y + dir[0], nx = s.x + dir[1];
                if (0 <= ny && ny < leny && 0 <= nx && nx < lenx && maze[ny][nx] != '#') {
                    int nextScore = (dir[0] == s.dy && dir[1] == s.dx) ? ss.score+1 : ss.score+1001;
                    queue.offer(new ScoredStep(new Step(ny, nx, dir[0], dir[1]), nextScore));
                }
            }
        }
        return scores;
    }
}
