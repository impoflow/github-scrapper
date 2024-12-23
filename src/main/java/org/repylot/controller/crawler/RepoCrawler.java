package org.repylot.controller.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RepoCrawler implements Crawler {
    private final String divClass = "search-title";
    private final String aClass = "prc-Link-Link-85e08";

    @Override
    public ArrayList<String> getSubUrls(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements divs = doc.select("div." + divClass);

        return (ArrayList<String>) divs.stream().map(div -> div.selectFirst("a." + aClass))
                .map(link -> link.attr("href"))
                .map(href -> "https://github.com" + href)
                .collect(Collectors.toList());
    }
}
