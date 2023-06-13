package org.consoleApp.dao;

import org.consoleApp.domin.Course;

import java.util.Optional;

public interface CourseDAO extends GenericDAO<Course> {
    Optional<Course> findByCourseName(String courseName);
}
