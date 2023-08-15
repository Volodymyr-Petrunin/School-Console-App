package org.consoleApp.dao.services;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.domin.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseDAO courseDAO;

    @Autowired
    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public Optional<Course> getCourseById(int courseId) {
        return courseDAO.findById(courseId);
    }

    public boolean createCourse(Course course) {
        return courseDAO.insert(course);
    }

    public void createMultipleCourses(List<Course> courses) {
        courseDAO.insertBatch(courses);
    }

    public boolean updateCourse(Course course) {
        return courseDAO.update(course);
    }

    public void deleteCourse(int courseId) {
        courseDAO.findById(courseId).ifPresent(courseDAO::delete);
    }

    public List<Course> getAllCoursesByStudentId(int studentId) {
        return courseDAO.findAllCourseByStudentsId(studentId);
    }
}
