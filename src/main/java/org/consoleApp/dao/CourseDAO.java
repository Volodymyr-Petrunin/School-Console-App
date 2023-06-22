package org.consoleApp.dao;

import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;

import java.util.List;
import java.util.Optional;

public interface CourseDAO extends GenericDAO<Course> {
    Optional<Course> findByCourseName(String courseName);
    List<Integer> findAllCourseIdByStudentsId(int studentId);
}
