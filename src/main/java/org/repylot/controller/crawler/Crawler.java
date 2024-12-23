package org.repylot.controller.crawler;

import java.io.IOException;
import java.util.ArrayList;

public interface Crawler {
    ArrayList<String> getSubUrls(String url) throws IOException;
}
