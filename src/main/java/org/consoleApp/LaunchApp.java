package org.consoleApp;

import org.consoleApp.courses.Course;
import org.consoleApp.courses.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.fillingData.CoursesDataFiller;
import org.consoleApp.fillingData.EnrollmentsDataFiller;
import org.consoleApp.fillingData.GroupDataFiller;
import org.consoleApp.fillingData.StudentsDataFiller;
import org.consoleApp.groups.Group;
import org.consoleApp.groups.GroupsDAOImpl;
import org.consoleApp.students.Student;
import org.consoleApp.students.StudentsDAOImpl;

import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class LaunchApp {
    private final DBConnector dbConnector = new DBConnector();
    private final String scriptCreateTables = "src\\main\\resources\\SQLScript\\create_tables.sql";
    private final ScriptRunner scriptRunner = new ScriptRunner(dbConnector);
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dbConnector);
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dbConnector);
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dbConnector);
    private final GroupDataFiller groupDataFiller = new GroupDataFiller(10);
    private final StudentsDataFiller studentsDataFiller = new StudentsDataFiller(200);
    private final CoursesDataFiller coursesDataFiller = new CoursesDataFiller();
    private final EnrollmentsDataFiller enrollmentsDataFiller = new EnrollmentsDataFiller(3);
    private final Scanner scan = new Scanner(System.in);
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything
    private boolean exit = false;
    public void launch(){

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
        menu.add(dash);

        return menu.toString();
    }

    private void userChooses(){
        System.out.print("Yor choice: ");
        int currentChose = scan.nextInt();

        if (currentChose == 1){
            findGroupsWithLessOrEqualStudents();
        }else if (currentChose == 2){
            List<Course> allCourses = courseDAO.findAll();
            System.out.println("All Courses:");
            for (Course course : allCourses) {
                System.out.println(course);
            }
        }else if (currentChose == 3){
            List<Student> allCourses = studentsDAO.findAll();
            System.out.println("All Courses:");
            for (Student student : allCourses) {
                System.out.println(student);
            }
        }else if (currentChose == 4){

        }else if (currentChose == 5){

        }else if (currentChose == 6){

        } else {
            exit = true;
        }
    }

    public void fillData(){
        scriptRunner.runScript(scriptCreateTables);

        coursesDataFiller.fillData();
        groupDataFiller.fillData();
        studentsDataFiller.fillData();
        enrollmentsDataFiller.fillData();
    }

    private void findGroupsWithLessOrEqualStudents(){
        System.out.println("Now write how many students should be in one group at least and I will try to find such groups :)");
        int maxStudentsInGroup = scan.nextInt();

        List<Group> allCourses = groupsDAO.findGroupsWithLessOrEqualStudents(maxStudentsInGroup);
        if (!allCourses.isEmpty()) {
            System.out.println("All Group:");
            for (Group group : allCourses) {
                System.out.println("Group name: " + group.groupName() + " and group id " + group.groupId());
            }
            System.out.println(dash);
        }else {
            System.out.println("No find groups!");
            System.out.println(dash);
        }
    }

}
