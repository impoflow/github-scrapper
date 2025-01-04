package org.repylot.model;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public final User owner;
    public final String name;
    public final String path;
    public final List<User> collaborators;

    public Project(User owner, String name, String path, List<User> collaborators) {
        this.owner = owner;
        this.name = name;
        this.path = path;
        this.collaborators = collaborators;
    }

    @Override
    public String toString() {
        List<String> collaboratorNames = new ArrayList<>();
        for (User collaborator : collaborators) {
            collaboratorNames.add("\"" + collaborator.name + "\"");
        }

        return "{" +
                "\"user\"= \"" + owner.name +
                "\", \"project_name\"= \"" + name +
                "\", collaborators= [" + String.join(", ", collaboratorNames) +
                "]}";
    }
}
