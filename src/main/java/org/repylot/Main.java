package org.repylot;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.repylot.controller.Controller;

import java.net.URL;

public class Main {
    public static void main(String[] args) {
        new Controller().run();
    }
}