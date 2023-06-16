package org.consoleApp.dataFilling.impl;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;

import java.util.List;
import java.util.Random;

public class EnrollmentsDataFiller implements DataFiller {
    private final Random random = new Random();
    private int numberOfStudentInOneCourse;
    private StudentsDAO studentsDAO;
    private CourseDAO courseDAO;

    public EnrollmentsDataFiller(int numberOfStudentInOneCourse, StudentsDAO studentsDAO, CourseDAO courseDAO) {
        this.numberOfStudentInOneCourse = numberOfStudentInOneCourse;
        this.studentsDAO = studentsDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public void fillData() {
        List<Student> students = studentsDAO.findAll();
        List<Course> courses = courseDAO.findAll();

        for (Student student : students){
            int numberOfCourse = getNumberOfCourses();

            for (int currentIndex = 0; currentIndex < numberOfCourse; currentIndex++){
                Course course = getRandomCourse(courses);

                studentsDAO.enrollStudentInCourse(student.getId(), course.getId());
            }
        }
    }

    private int getNumberOfCourses(){
        return random.nextInt(numberOfStudentInOneCourse) + 1;
    }

    private Course getRandomCourse(List<Course> courses){
        int index = random.nextInt(courses.size());
        return courses.get(index);
    }
}
