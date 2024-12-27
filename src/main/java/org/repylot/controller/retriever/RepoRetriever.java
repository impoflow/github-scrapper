package org.repylot.controller.retriever;

import org.repylot.Model.Project;
import org.repylot.Model.User;
import org.repylot.controller.Controller;
import org.repylot.controller.datalake.DataLakeWriter;
import org.repylot.controller.datalake.FileDataLakeWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        Project project = project(codeUrl);
        System.out.println(project);

        try {
            InputStream inputStream = connection.getInputStream();
            byte[] fileBytes = inputStream.readAllBytes();

            InputStream inputStreamZip = new ByteArrayInputStream(fileBytes);
            writer.save(inputStreamZip, project.owner.name + '/' + project.path);
            inputStreamZip.close();

            InputStream projectInfoStream = new ByteArrayInputStream(project.toString().getBytes(StandardCharsets.UTF_8));
            writer.save(projectInfoStream, project.owner.name + '/' + project.name + ".info");
            inputStream.close();
        } catch (OutOfMemoryError e) {
            System.gc();
        }

        connection.disconnect();
    }

    private static Project project(String codeUrl) {
        User projectOwner = getProjectOwner();
        String name = codeUrl.replace("https://github.com/", "")
                .replace("/", "").replace(".zip", "");
        String path = name + ".zip";
        List<User> collaborators = getCollaborators(Controller.users, projectOwner);

        return new Project(projectOwner, name, path, collaborators);
    }

    private static User getProjectOwner() {
        Random random = new Random();
        int randomIndex = random.nextInt(Controller.users.size());
        User owner = Controller.users.get(randomIndex);
        return owner;
    }

    public static List<User> getCollaborators(List<User> users, User chosenUser) {
        List<User> collaborators = new ArrayList<>();
        Random random = new Random();

        double prob = random.nextDouble();
        double collaboratorProb = random.nextDouble()*0.1;

        if (prob > 0.5)
            for (User user : users) {
                if (!user.name.equals(chosenUser.name) &&  random.nextDouble() < collaboratorProb) {
                    collaborators.add(user);
                }
            }

        return collaborators;
    }

}
