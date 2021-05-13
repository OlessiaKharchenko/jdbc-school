package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileConverter {
    private ResourceFileReader reader;

    public FileConverter(ResourceFileReader reader) {
        this.reader = reader;
    }

    public List<String> convertFileToList(String fileName) throws IOException, URISyntaxException {
        return Arrays.stream(reader.readFile(fileName)
                .split(System.lineSeparator()))
                .collect(Collectors.toList());
    }
}