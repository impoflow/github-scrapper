package org.repylot.controller.retriever;

import org.repylot.controller.datalake.DataLakeWriter;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RepoRetriever implements Retriever {
    private DataLakeWriter writer;

    public RepoRetriever(DataLakeWriter writer) {
        this.writer = writer;
    }

    public void retrieve(String url) throws IOException {
        String codeUrl = url + "/archive/refs/heads/master.zip";
        System.out.println(codeUrl);

        URL downloadUrl = new URL(codeUrl);

        HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file: HTTP ");
        }

        InputStream inputStream = connection.getInputStream();
        String outputFilePath = codeUrl.replace("https://github.com/", "")
                .replace("/", "");

        writer.save(inputStream, outputFilePath);

        inputStream.close();
        connection.disconnect();
    }

}
