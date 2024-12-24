package org.repylot.controller.datalake;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;

public class AmazonS3Writer implements DataLakeWriter {
    private final AmazonS3 s3;
    private final String bucketName;

    public AmazonS3Writer(String bucketName, String region) {
        this.s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .build();
        this.bucketName = bucketName;
    }

    @Override
    public void save(InputStream inputStream, String outputFilePath) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/octet-stream");
            metadata.setContentLength(inputStream.available());

            s3.putObject(bucketName, outputFilePath, inputStream, metadata);

            System.out.println("File uploaded to S3: " + bucketName + "/" + outputFilePath);
        } catch (Exception e) {
            throw new IOException("Error posting file", e);
        }
    }
}
