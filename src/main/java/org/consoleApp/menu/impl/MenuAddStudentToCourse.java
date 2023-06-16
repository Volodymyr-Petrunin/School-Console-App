package org.consoleApp.menu.impl;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.helper.PrintInfo;

import java.util.List;
import java.util.Scanner;

public class MenuAddStudentToCourse implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private final PrintInfo printInfo = new PrintInfo();
    private StudentsDAO studentsDAO;
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private String dash;

    public MenuAddStudentToCourse(StudentsDAO studentsDAO, CourseDAO courseDAO, GroupDAO groupDAO,String dash) {
        this.studentsDAO = studentsDAO;
        this.courseDAO = courseDAO;
        this.groupDAO = groupDAO;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Add a student to a course";
    }

    @Override
    public void execute() {
        List<Student> students = studentsDAO.findAll();
        List<Course> courses = courseDAO.findAll();

        System.out.println("All students:");
        printInfo.printStudents(students, groupDAO);

        System.out.print("Now choose student id: ");
        int studentId = scan.nextInt();

        System.out.println("All courses:");
        printInfo.printCourses(courses);

        System.out.print("Now choose courses id: ");
        int coursesId = scan.nextInt();

        boolean addStudentSuccess = studentsDAO.enrollStudentInCourse(studentId, coursesId);

        if (addStudentSuccess){
            System.out.println("Add student successfully!");
            System.out.println(dash);
        }else {
            System.out.println("Something wrong! :(");
            System.out.println(dash);
        }
    }
}
