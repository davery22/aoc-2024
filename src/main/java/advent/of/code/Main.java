package advent.of.code;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.FormatProcessor.FMT;

public class Main {
    public static void main(String[] args) throws Exception {
        String red = "31", green = "32";
        for (int i = 1; i <= 25; i++) {
            Class<?> clazz;
            try {
                // Un-discoverable reflective shenanigans
                clazz = Class.forName(FMT."advent.of.code.days.Day%02d\{i}");
            } catch (ClassNotFoundException e) {
                return;
            }
            System.out.println(STR."\033[\{i % 2 == 0 ? red : green}mOn the \{i}\{suffix(i)} day of Christmas...\033[0m");
            Day day = (Day) clazz.getConstructors()[0].newInstance();
            try (AdventIO io = new AdventIO(FMT."/day%02d\{i}.txt")) {
                time(() -> day.part1(io));
            }
            try (AdventIO io = new AdventIO(FMT."/day%02d\{i}.txt")) {
                time(() -> day.part2(io));
            }
        }
        System.out.println("\033[33mMerry Christmas!\033[0m");
    }
    
    static void time(Runnable r) {
        Instant start = Instant.now();
        r.run();
        Instant end = Instant.now();
        System.out.println(STR."\033[37m--- \{Duration.between(start, end)} ---\033[0m");
    }
    
    static String suffix(int i) {
        return (i/10%10 == 1) ? "th" : switch (i%10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
    
    static class AdventIO implements IO, AutoCloseable {
        // This class late-initializes the input resource on first call to lines() or scanner().
        // It tracks the resource, so that the immediate caller need not be responsible for closing it.
        
        final String resourceFileName;
        AutoCloseable resource;
        
        AdventIO(String resourceFileName) {
            this.resourceFileName = resourceFileName;
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public Stream<String> lines() {
            if (resource != null) {
                throw new IllegalStateException("Input already initialized");
            }
            // Though we could read lines from a scanner, the splitting behavior of Files.lines().parallel() is better.
            try {
                URI uri = Main.class.getResource(resourceFileName).toURI();
                return (Stream<String>) (resource = Files.lines(Paths.get(uri)));
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public Scanner scanner() {
            if (resource != null) {
                throw new IllegalStateException("Input already initialized");
            }
            return (Scanner) (resource = new Scanner(Main.class.getResourceAsStream(resourceFileName)));
        }
        
        @Override
        public PrintStream out() {
            return System.out;
        }
        
        @Override
        public void close() throws Exception {
            if (resource != null) {
                resource.close();
            }
        }
    }
}
