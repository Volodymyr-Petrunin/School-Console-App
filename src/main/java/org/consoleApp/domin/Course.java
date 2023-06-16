package org.consoleApp.domin;

public class Course {
    private Integer id;
    private String name;
    private String description;

    public Course(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int courseId) {
        this.id = courseId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
