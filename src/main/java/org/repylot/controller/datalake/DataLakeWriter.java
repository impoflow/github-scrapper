package org.repylot.controller.datalake;

import java.io.IOException;
import java.io.InputStream;

public interface DataLakeWriter {
    void save(InputStream inputStream, String outputFilePath) throws IOException;
}
