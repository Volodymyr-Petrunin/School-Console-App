package org.consoleApp.menu.leaf;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.menu.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuDeleteStudent implements MenuItem {
    private StudentsDAO studentsDAO;
    private String dash;

    @Autowired
    public MenuDeleteStudent(StudentsDAO studentsDAO , String dash) {
        this.studentsDAO = studentsDAO;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Delete a student by STUDENT_ID";
    }

    @Override
    public void execute() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Now write the id of the student you wont to delete ;)");
        int studentId = scan.nextInt();

        if (studentId != 0){
            boolean deleteStudentsSuccessful = studentsDAO.deleteByStudentId(studentId);

            if (deleteStudentsSuccessful){
                System.out.println("Delete student successfully!");
                System.out.print(dash);
            }else {
                System.out.println("Something wrong! :(");
                System.out.print(dash);
            }
        }
    }
}
