package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


class ResourceFileReaderTest {

    private ResourceFileReader reader = new ResourceFileReader();

    @Test
    void readFile_shouldThrowException_whenFileDoesNotExist() {
        assertThrows(RuntimeException.class, () -> reader.readFile("non-existent-file.log"));
    }

    @Test
    void readFile_shouldThrowException_whenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> reader.readFile(null));
    }

    @Test
    void readFile_shouldThrowException_whenIsEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> reader.readFile(""));
    }
}