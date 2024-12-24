package org.repylot.controller;

import org.repylot.controller.datalake.AmazonS3Writer;
import org.repylot.controller.datalake.DataLakeWriter;
import org.repylot.controller.retriever.RepoRetriever;
import org.repylot.controller.crawler.RepoCrawler;
import org.repylot.controller.task.MultipleScrappingTask;

import java.util.Timer;

public class Controller {

    public void run() {
        String name = System.getenv("BUCKET_NAME");
        String region = System.getenv("REGION");

        System.out.println("Bucket: " + name);
        System.out.println("Region: " + region);
        DataLakeWriter writer = new AmazonS3Writer(name, region);

        Timer timer = new Timer();
        timer.schedule(
                new MultipleScrappingTask(
                        new RepoCrawler(),
                        new RepoRetriever(writer),
                        "https://github.com/search?q=%23java&type=repositories%3Fp%3D2&p="
                ), 0, 2000);
    }
}
