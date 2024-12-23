package org.repylot.controller;

import org.repylot.controller.datalake.FileDataLakeWriter;
import org.repylot.controller.retriever.RepoRetriever;
import org.repylot.controller.crawler.RepoCrawler;
import org.repylot.controller.task.MultipleScrappingTask;

import java.util.Timer;

public class Controller {

    public void run() {
        Timer timer = new Timer();
        timer.schedule(
                new MultipleScrappingTask(
                        new RepoCrawler(),
                        new RepoRetriever(new FileDataLakeWriter()),
                        "https://github.com/search?q=%23java&type=repositories%3Fp%3D2&p="
                ), 0, 2000);
    }
}
