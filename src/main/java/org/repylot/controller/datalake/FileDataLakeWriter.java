package org.repylot.controller.datalake;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDataLakeWriter implements DataLakeWriter {
    private final String dataLakePath = "datalake";

    @Override
    public void save(InputStream inputStream, String file) throws IOException {
        String[] pathParts = file.split("/");
        String currentPath = dataLakePath;

        for (int i = 0; i < pathParts.length - 1; i++) {
            currentPath += File.separator + pathParts[i];
            Files.createDirectories(Paths.get(currentPath));
        }

        String outputFile = currentPath + File.separator + pathParts[pathParts.length - 1];

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
