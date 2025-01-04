package org.repylot.controller.retriever;

import org.repylot.model.Project;
import org.repylot.model.User;
import org.repylot.controller.Controller;
import org.repylot.controller.datalake.DataLakeWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
            throw new IOException("Failed to download file: HTTP " + connection.getResponseCode());
        }

        Project project = project(codeUrl);
        System.out.println(project);

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] fileBytes = inputStream.readAllBytes();
            boolean hasSrcDirectory = containsSrcDirectory(fileBytes);

            if (hasSrcDirectory) {
                System.out.println("ZIP file contains src directory. Saving project...");

                System.out.println(project.toString());

                InputStream inputStreamZip = new ByteArrayInputStream(fileBytes);
                writer.save(inputStreamZip, project.owner.name + '/' + project.path);
                inputStreamZip.close();

                InputStream projectInfoStream = new ByteArrayInputStream(
                        project.toString().getBytes(StandardCharsets.UTF_8));
                writer.save(projectInfoStream, project.owner.name + '/' + project.name + ".json");
            } else {
                System.out.println("ZIP file doesn't contain src directory. Project not saved.");
            }
        } catch (OutOfMemoryError e) {
            System.gc();
        }

        connection.disconnect();
    }

    private boolean containsSrcDirectory(byte[] fileBytes) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(fileBytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String normalizedName = entry.getName().replace("\\", "/");
                if (entry.isDirectory() && normalizedName.contains("/src")) {
                    return true;
                }
            }
        }
        return false;
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
        return Controller.users.get(randomIndex);
    }

    public static List<User> getCollaborators(List<User> users, User chosenUser) {
        List<User> collaborators = new ArrayList<>();
        Random random = new Random();

        double prob = random.nextDouble();
        double collaboratorProb = random.nextDouble() * 0.1;

        if (prob > 0.5) {
            for (User user : users) {
                if (!user.name.equals(chosenUser.name) && random.nextDouble() < collaboratorProb) {
                    collaborators.add(user);
                }
            }
        }

        return collaborators;
    }
}
