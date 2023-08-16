package org.consoleApp.generation.impl;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.generation.records.EnrollInfo;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnrollmentsDataGeneration implements GenerationData<EnrollInfo> {
    private final Random random = new Random();
    private int numberOfStudentInOneCourse;
    private StudentsDAO studentsDAO;
    private CourseDAO courseDAO;

    public EnrollmentsDataGeneration(int numberOfStudentInOneCourse, StudentsDAO studentsDAO, CourseDAO courseDAO) {
        this.numberOfStudentInOneCourse = numberOfStudentInOneCourse;
        this.studentsDAO = studentsDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public List<EnrollInfo> generateData() {
        List<Student> students = studentsDAO.findAll();
        List<Course> courses = courseDAO.findAll();
        List<EnrollInfo> enrollInfo = new ArrayList<>();

        for (Student student : students){
            int numberOfCourse = getNumberOfCourses();

            for (int currentIndex = 0; currentIndex < numberOfCourse; currentIndex++){
                Course course = getRandomCourse(courses);

                enrollInfo.add(new EnrollInfo(student.getId(), course.getId()));
            }
        }
        return enrollInfo;
    }

    private int getNumberOfCourses() {
        return random.nextInt(numberOfStudentInOneCourse) + 1;
    }

    private Course getRandomCourse(List<Course> courses) {
        int index = random.nextInt(courses.size());
        return courses.get(index);
    }
}
