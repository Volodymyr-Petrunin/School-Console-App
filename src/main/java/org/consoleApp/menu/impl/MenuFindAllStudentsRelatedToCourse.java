package org.consoleApp.menu.impl;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.jdbc.Enrollments;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.helper.PrintInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuFindAllStudentsRelatedToCourse implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private final PrintInfo printInfo = new PrintInfo();
    private CourseDAO courseDAO;
    private Enrollments enrollmentsDAO;
    private StudentsDAO studentsDAO;
    private GroupDAO groupDAO;
    private String dash;

    public MenuFindAllStudentsRelatedToCourse(CourseDAO courseDAO, Enrollments enrollmentsDAO, StudentsDAO studentsDAO, GroupDAO groupDAO, String dash) {
        this.courseDAO = courseDAO;
        this.enrollmentsDAO = enrollmentsDAO;
        this.studentsDAO = studentsDAO;
        this.groupDAO = groupDAO;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Find all students related to a course with a specified name";
    }

    @Override
    public void execute() {
        System.out.println("Now write the name of course 0_0");
        String courseName = scan.next();
        Optional<Course> optionalCourse = courseDAO.findByCourseName(courseName);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<Integer> studentsId = enrollmentsDAO.findAllStudentsIdByCourseId(course.getId());

            List<Student> students = new ArrayList<>();

            for (Integer currentId : studentsId) {
                studentsDAO.findById(currentId).ifPresent(students::add);
            }

            System.out.println("All Students: ");
            printInfo.printStudents(students, groupDAO);

            System.out.println(dash);
        } else {
            System.out.println("Wrong course name :(");
        }
    }
}
