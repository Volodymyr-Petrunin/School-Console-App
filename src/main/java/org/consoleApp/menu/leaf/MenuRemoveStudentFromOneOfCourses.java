package org.consoleApp.menu.leaf;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.helper.PrintInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class MenuRemoveStudentFromOneOfCourses implements MenuItem {
    private final PrintInfo printInfo = new PrintInfo();
    private StudentsDAO studentsDAO;
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private String dash;

    @Autowired
    public MenuRemoveStudentFromOneOfCourses(StudentsDAO studentsDAO,  CourseDAO courseDAO, GroupDAO groupDAO, String dash) {
        this.studentsDAO = studentsDAO;
        this.courseDAO = courseDAO;
        this.groupDAO = groupDAO;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Remove a student from one of their courses";
    }

    @Override
    public void execute() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter student name: ");
        String studentName = scan.nextLine();

        List<Student> foundedStudents = studentsDAO.findByFirstName(studentName);

        System.out.println("I find " + foundedStudents.size() + " students:");

        printInfo.printStudents(foundedStudents, groupDAO);

        if (!foundedStudents.isEmpty()) {
            System.out.println("Now choose which one you need and write its id: ");
            int studentId = scan.nextInt();

            List<Course> courses = courseDAO.findAllCourseByStudentsId(studentId);

            System.out.println("Now select the course id you want to remove from your student: ");

            printInfo.printCourses(courses);

            int courseId = scan.nextInt();

            boolean deleteFromCourseSuccessful = studentsDAO.removeStudentFromCourse(studentId, courseId);

            if (deleteFromCourseSuccessful) {
                System.out.println("Delete student successfully!");
                System.out.print(dash);
            } else {
                System.out.println("Something wrong! :(");
                System.out.print(dash);
            }
        }
    }
}
