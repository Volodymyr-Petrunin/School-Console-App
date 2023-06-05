package org.consoleApp;

import org.consoleApp.Courses.CourseDAOImpl;
import org.consoleApp.DataBaseSettings.DBConnector;
import org.consoleApp.DataBaseSettings.ScriptRunner;

import java.util.Scanner;
import java.util.StringJoiner;

public class LaunchApp {
    private final DBConnector dbConnector = new DBConnector();
    private final String scriptCreateTables = "src\\main\\resources\\SQLScript\\create_tables.sql";
    private final ScriptRunner scriptRunner = new ScriptRunner(dbConnector);
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dbConnector);
    private boolean exit = false;
    public void launch(){
        scriptRunner.runScript(scriptCreateTables);

        while (!exit){
            System.out.println(menu());
            userChooses();
        }
    }

    private String menu(){
        StringJoiner menu = new StringJoiner(System.lineSeparator());

        menu.add("Please select an option:");
        menu.add("1. Find all groups with less or equal number of students");
        menu.add("2. Find all students related to a course with a specified name");
        menu.add("3. Add a new student");
        menu.add("4. Delete a student by STUDENT_ID");
        menu.add("5. Add a student to a course");
        menu.add("6. Remove a student from one of their courses");
        menu.add("");
        menu.add("0. Exit");
        menu.add("-".repeat(50)); // yes magic number. But it doesn't affect anything

        return menu.toString();
    }

    private void userChooses(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Yor choice: ");
        int currentChose = scan.nextInt();

        if (currentChose == 1){

        }else if (currentChose == 2){

        }else if (currentChose == 3){

        }else if (currentChose == 4){

        }else if (currentChose == 5){

        }else if (currentChose == 6){

        } else {
            exit = true;
        }

    }

}
