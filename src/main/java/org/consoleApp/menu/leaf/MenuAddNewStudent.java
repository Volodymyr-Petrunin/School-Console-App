package org.consoleApp.menu.leaf;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.menu.MenuItem;

import java.util.List;
import java.util.Scanner;

public class MenuAddNewStudent implements MenuItem {
    private GroupDAO groupDAO;
    private StudentsDAO studentsDAO;
    private String dash;

    public MenuAddNewStudent(GroupDAO groupDAO, StudentsDAO studentsDAO, String dash) {
        this.groupDAO = groupDAO;
        this.studentsDAO = studentsDAO;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Add a new student";
    }

    @Override
    public void execute() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter student details =)");

        System.out.println("First Name: ");
        String firstName = scan.nextLine();

        System.out.println("Last Name: ");
        String lastName = scan.nextLine();

        System.out.println("All groups: ");
        List<Group> allGroups = groupDAO.findAll();

        for (Group group : allGroups){
            System.out.println("Group name: " + group.getName());
        }

        System.out.println("Choose group name: ");
        String groupName = scan.nextLine();

        int groupId = findGroupIdByName(allGroups, groupName);

        Student newStudent = new Student(null,groupId,firstName,lastName);

        boolean operationSuccessful = studentsDAO.insert(newStudent);
        boolean enrollSuccessful = studentsDAO.enrollStudentInCourse(newStudent.getId(), groupId);

        if (operationSuccessful && enrollSuccessful){
            System.out.println("New student added successfully! :)");
            System.out.print(dash);
        }else {
            System.out.println("Something wrong! :(");
            System.out.print(dash);
        }
    }

    private int findGroupIdByName(List<Group> groups, String groupName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group.getId();
            }
        }
        throw new IllegalArgumentException("Can't find group id by group name: " + groupName + "in LaunchApp");
    }
}
