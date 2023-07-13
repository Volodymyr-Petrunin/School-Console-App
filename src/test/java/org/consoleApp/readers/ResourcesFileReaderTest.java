package org.consoleApp.readers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesFileReaderTest {
    private static final String fileName = "data.txt";
    private final ResourcesFileReader resourcesFileReader = new ResourcesFileReader(fileName);

    @Test
    void testReader_ShouldReturnRuntimeException_WhenInputWrongFileName(){
        ResourcesFileReader wrongFileName = new ResourcesFileReader("nonexistent_file.txt");
        assertThrows(RuntimeException.class, wrongFileName::read);
    }

    @Test
    void testReader_ShouldReadFileContents_WhenInputValidFileName(){
        List<String> expectedLines = List.of(
            "Mathematics_This course about Math.",
            "Biology_This course about Biology.",
            "Physics_This course about Physics."
        );

        List<String> actualLines = resourcesFileReader.read();

        assertEquals(expectedLines, actualLines);
    }

}