package org.consoleApp.courses;

import java.util.List;

public interface CourseDAO {
    List<Course> findAll();
    Course findById(int courseId);
    void insertNewCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(Course course);
    Course findByCourseName(String courseName);
    int getNextCourseId();
}
