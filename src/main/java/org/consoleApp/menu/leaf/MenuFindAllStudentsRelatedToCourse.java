package org.consoleApp.menu.leaf;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Student;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.helper.PrintInfo;

import java.util.List;
import java.util.Scanner;

public class MenuFindAllStudentsRelatedToCourse implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private final PrintInfo printInfo = new PrintInfo();
    private StudentsDAO studentsDAO;
    private GroupDAO groupDAO;
    private String dash;

    public MenuFindAllStudentsRelatedToCourse(StudentsDAO studentsDAO, GroupDAO groupDAO, String dash) {
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

        List<Student> students = studentsDAO.findStudentsByCourseName(courseName);
        if (!students.isEmpty()) {
            System.out.println("All Students: ");
            printInfo.printStudents(students, groupDAO);
        } else {
            System.out.println(dash);

            System.out.println("Wrong course name :(");
        }
    }
}
