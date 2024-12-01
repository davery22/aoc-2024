package advent.of.code.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Utils {
    private Utils() {}
    
    public static Stream<String> lines(String resourceFileName) throws URISyntaxException, IOException {
        return Files.lines(Paths.get(Utils.class.getResource(resourceFileName).toURI()));
    }
}
