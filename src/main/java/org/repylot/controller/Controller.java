package org.repylot.controller;

import org.repylot.controller.datalake.FileDataLakeWriter;
import org.repylot.model.User;
import org.repylot.controller.datalake.AmazonS3Writer;
import org.repylot.controller.datalake.DataLakeWriter;
import org.repylot.controller.retriever.RepoRetriever;
import org.repylot.controller.crawler.RepoCrawler;
import org.repylot.controller.task.MultipleScrappingTask;

import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller {
    public static final List<User> users = getUsers();

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
                        "https://github.com/search?q=java+agenda&type=repositories"
                ), 0, 10000);
    }

    public static List<User> getUsers() {
        return IntStream.range(1, 101)
                .mapToObj(i -> new User("User_" + i))
                .collect(Collectors.toList());
    }
}
