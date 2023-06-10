package org.consoleApp.courses;

import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    List<Course> findAll();
    Optional<Course> findById(int courseId);
    void insert(Course course);
    void update(Course course);
    void delete(Course course);
    Optional<Course> findByCourseName(String courseName);
}
