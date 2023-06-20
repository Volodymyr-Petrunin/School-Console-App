package org.consoleApp.domin;

import java.util.Optional;

public class Student {
    private Integer id;
    private Integer groupId;
    private String firstName;
    private String lastName;

    public Student(Integer id, Integer groupId, String firstName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Optional<Integer> getGroupId() {
        return Optional.ofNullable(groupId);
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
