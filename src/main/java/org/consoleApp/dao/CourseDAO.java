package org.consoleApp.dao;

import org.consoleApp.domin.Course;

import java.util.List;

public interface CourseDAO extends GenericDAO<Course> {
    List<Course> findAllCourseByStudentsId(int studentId);
}
