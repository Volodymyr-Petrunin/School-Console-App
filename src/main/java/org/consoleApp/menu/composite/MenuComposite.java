package org.consoleApp.menu.composite;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.leaf.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class MenuComposite implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private List<MenuItem> menuItems;
    private boolean exit;
    private GroupDAO groupsDAO;
    private CourseDAO courseDAO;
    private StudentsDAO studentsDAO;
    private String dash;

    public MenuComposite(GroupDAO groupsDAO, CourseDAO courseDAO, StudentsDAO studentsDAO, String dash) {
        this.groupsDAO = groupsDAO;
        this.courseDAO = courseDAO;
        this.studentsDAO = studentsDAO;
        this.dash = dash;
        this.exit = false;
        this.menuItems = createMenuItems();
    }

    @Override
    public String getDescription() {
        return "Please select an option: ";
    }

    @Override
    public void execute() {
        while (!exit){
            displayMenu();
            userChooses();
        }
    }

    private void displayMenu() {
        StringJoiner menu = new StringJoiner(System.lineSeparator());

        menu.add(getDescription());

        int place = 1;
        for (MenuItem item : menuItems) {
            menu.add(place++ + ". " + item.getDescription());
        }

        menu.add("");
        menu.add("0. Exit");
        menu.add(dash);

        System.out.println(menu);
    }

    private void userChooses(){
        System.out.print("Yor choice: ");
        int currentChoice = scan.nextInt();

        if (currentChoice >= 1 && currentChoice <= menuItems.size()) {
            menuItems.get(currentChoice - 1).execute();
        } else if (currentChoice == 0) {
            exit = true;
        }
    }

    private List<MenuItem> createMenuItems(){
        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuFindGroupsWithLessOrEqualStudents(groupsDAO, dash));
        menu.add(new MenuFindAllStudentsRelatedToCourse(courseDAO, studentsDAO, groupsDAO, dash));
        menu.add(new MenuAddNewStudent(groupsDAO, studentsDAO, dash));
        menu.add(new MenuDeleteStudent(studentsDAO, dash));
        menu.add(new MenuAddStudentToCourse(studentsDAO, courseDAO, groupsDAO, dash));
        menu.add(new MenuRemoveStudentFromOneOfCourses(studentsDAO, courseDAO, groupsDAO, dash));

        return menu;
    }
}
