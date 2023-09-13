package org.consoleApp.domin;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer id;
    @Column(name = "group_id", insertable=false, updatable=false)
    private Integer groupId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @ManyToMany
    @JoinTable(name = "enrollments",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {
    }

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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(student.groupId, 0) || Objects.equals(student.groupId, null)
                || Objects.equals(groupId, student.groupId) && Objects.equals(firstName, student.firstName)
                && Objects.equals(lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, firstName, lastName);
    }
}
