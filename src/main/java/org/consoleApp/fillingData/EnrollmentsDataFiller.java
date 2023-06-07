package org.consoleApp.fillingData;

import org.consoleApp.courses.Course;
import org.consoleApp.courses.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.enrollments.EnrollmentsDAOImpl;
import org.consoleApp.students.Student;
import org.consoleApp.students.StudentsDAOImpl;

import java.util.List;
import java.util.Random;

public class EnrollmentsDataFiller implements DataFiller{
    private final Random random = new Random();
    private final DBConnector dbConnector = new DBConnector();
    private final EnrollmentsDAOImpl enrollmentsDAO = new EnrollmentsDAOImpl(dbConnector);
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dbConnector);
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dbConnector);
    int numberOfStudentInOneCourse;
    public EnrollmentsDataFiller(int numberOfStudentInOneCourse) {
        this.numberOfStudentInOneCourse = numberOfStudentInOneCourse;
    }

    @Override
    public void fillData() {
        List<Student> students = studentsDAO.findAll();
        List<Course> courses = courseDAO.findAll();

        for (Student student : students){
            int numberOfCourse = getNumberOfCourses();

            for (int currentIndex = 0; currentIndex < numberOfCourse; currentIndex++){
                Course course = getRandomCourse(courses);

                enrollmentsDAO.enrollStudentInCourse(student.student_id(), course.courseId());
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
