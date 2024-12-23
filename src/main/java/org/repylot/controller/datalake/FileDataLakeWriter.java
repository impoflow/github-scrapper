package org.repylot.controller.datalake;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDataLakeWriter implements DataLakeWriter {
    private final String dataLakePath = "datalake";

    @Override
    public void save(InputStream inputStream, String file) throws IOException {
        Files.createDirectories(Paths.get(dataLakePath));
        String outputFile = dataLakePath + File.separator + file;

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }

}
