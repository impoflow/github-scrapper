package org.repylot.controller.task;

import org.repylot.controller.crawler.Crawler;
import org.repylot.controller.retriever.Retriever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class MultipleScrappingTask extends TimerTask {
    private final Crawler crawler;
    private final Retriever retriever;

    private final String url;
    private int page;

    public MultipleScrappingTask(Crawler crawler, Retriever retriever, String url) {
        this.crawler = crawler;
        this.retriever = retriever;
        this.url = url;
        this.page = 1;
    }

    @Override
    public void run() {
        ArrayList<String> subUrls = getSubUrls();
        for (String url : subUrls) {
            System.out.println(url);
            retrieve(url);
        }
    }


    private void retrieve(String url) {
        try {
            retriever.retrieve(url);
        } catch (IOException e) {
            System.out.println("Exception saving " + url + " to Data Lake");
        }
    }

    private ArrayList<String> getSubUrls() {
        try {
            System.out.println(url + page++);
            return crawler.getSubUrls(url + page);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
