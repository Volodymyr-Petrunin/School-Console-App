package org.consoleApp.menu.leaf;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.jdbc.EnrollmentsDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.helper.PrintInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuRemoveStudentFromOneOfCourses implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private final PrintInfo printInfo = new PrintInfo();
    private StudentsDAO studentsDAO;
    private EnrollmentsDAO enrollmentsDAO;
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private String dash;

    public MenuRemoveStudentFromOneOfCourses(StudentsDAO studentsDAO, EnrollmentsDAO enrollmentsDAO, CourseDAO courseDAO, GroupDAO groupDAO, String dash) {
        this.studentsDAO = studentsDAO;
        this.enrollmentsDAO = enrollmentsDAO;
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
        System.out.print("Enter student name: ");
        String studentName = scan.next();

        List<Student> foundedStudents = studentsDAO.findByFirstName(studentName);

        System.out.println("I find " + foundedStudents.size() + " students:");

        printInfo.printStudents(foundedStudents, groupDAO);

        if (!foundedStudents.isEmpty()) {
            System.out.print("Now choose which one you need and write its id: ");
            int studentId = scan.nextInt();

            List<Integer> coursesId = enrollmentsDAO.findAllCourseIdByStudentsId(studentId);
            List<Course> courses = new ArrayList<>();

            for (Integer currentInt : coursesId) {
                courseDAO.findById(currentInt).ifPresent(courses::add);
            }

            System.out.println("Now select the course id you want to remove from your student: ");

            printInfo.printCourses(courses);

            int courseId = scan.nextInt();

            boolean deleteFromCourseSuccessful = enrollmentsDAO.removeStudentFromCourse(studentId, courseId);

            if (deleteFromCourseSuccessful) {
                System.out.println("Delete student successfully!");
                System.out.println(dash);
            } else {
                System.out.println("Something wrong! :(");
                System.out.println(dash);
            }
        }
    }
}
