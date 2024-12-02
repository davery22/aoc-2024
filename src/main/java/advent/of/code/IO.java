package advent.of.code;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface IO {
    Stream<String> lines();
    Scanner scanner();
    PrintStream out();
    
    Pattern EOL = Pattern.compile("\r\n|[\n\r\u2028\u2029\u0085]");
}
