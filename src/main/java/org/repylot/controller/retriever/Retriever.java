package org.repylot.controller.retriever;

import java.io.IOException;

public interface Retriever {
    void retrieve(String url) throws IOException;
}
