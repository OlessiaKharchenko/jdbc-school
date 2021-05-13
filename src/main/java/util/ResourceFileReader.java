package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ResourceFileReader {

    public String readFile(String fileName) throws URISyntaxException, IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("The input can't be null or empty");
        }
        URL url = getClass().getClassLoader().getResource(fileName);
        Path path = Paths.get(url.toURI());
        return Files.lines(path).collect(Collectors.joining(System.lineSeparator()));
    }
}