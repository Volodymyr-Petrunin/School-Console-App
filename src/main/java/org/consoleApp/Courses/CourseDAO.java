package org.consoleApp.Courses;

import java.util.List;

public interface CourseDAO {
    List<Course> findAll();
    Course findById(int courseId);
    void insert(Course course);
    void update(Course course);
    void delete(Course course);
}
