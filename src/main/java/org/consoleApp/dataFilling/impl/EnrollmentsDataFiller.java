package org.consoleApp.dataFilling.impl;

import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Course;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.domin.Student;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

public class EnrollmentsDataFiller implements DataFiller {
    private final Random random = new Random();
    private int numberOfStudentInOneCourse;
    private StudentsDAOImpl studentsDAO;
    private CourseDAOImpl courseDAO;

    public EnrollmentsDataFiller(int numberOfStudentInOneCourse, DataSource dataSource) {
        this.numberOfStudentInOneCourse = numberOfStudentInOneCourse;
        this.studentsDAO = new StudentsDAOImpl(dataSource);
        this.courseDAO = new CourseDAOImpl(dataSource);
    }

    @Override
    public void fillData() {
        List<Student> students = studentsDAO.findAll();
        List<Course> courses = courseDAO.findAll();

        for (Student student : students){
            int numberOfCourse = getNumberOfCourses();

            for (int currentIndex = 0; currentIndex < numberOfCourse; currentIndex++){
                Course course = getRandomCourse(courses);

                studentsDAO.enrollStudentInCourse(student.student_id(), course.courseId());
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
