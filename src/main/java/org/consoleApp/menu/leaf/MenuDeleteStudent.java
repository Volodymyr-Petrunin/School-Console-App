package org.consoleApp.menu.leaf;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.jdbc.EnrollmentsDAO;
import org.consoleApp.menu.MenuItem;

import java.util.Scanner;

public class MenuDeleteStudent implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private EnrollmentsDAO enrollmentsDAO;
    private StudentsDAO studentsDAO;
    private String dash;

    public MenuDeleteStudent(EnrollmentsDAO enrollmentsDAO, StudentsDAO studentsDAO , String dash) {
        this.enrollmentsDAO = enrollmentsDAO;
        this.studentsDAO = studentsDAO;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Delete a student by STUDENT_ID";
    }

    @Override
    public void execute() {
        System.out.println("Now write the id of the student you wont to delete ;)");
        int studentId = scan.nextInt();

        if (studentId != 0){
            boolean deleteFromEnrollSuccessful = enrollmentsDAO.deleteStudentById(studentId);
            boolean deleteFromStudentsSuccessful = studentsDAO.deleteByStudentId(studentId);

            if (deleteFromEnrollSuccessful && deleteFromStudentsSuccessful){
                System.out.println("Delete student successfully!");
                System.out.println(dash);
            }else {
                System.out.println("Something wrong! :(");
                System.out.println(dash);
            }
        }
    }
}
