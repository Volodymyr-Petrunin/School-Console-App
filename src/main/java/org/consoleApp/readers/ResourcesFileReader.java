package org.consoleApp.readers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourcesFileReader implements Reader{
    private String fileName;
    public ResourcesFileReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> read() {
        InputStream inputStream = getClass().getResourceAsStream("/" +  fileName);

        assert inputStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return reader.lines().toList();
    }
}
