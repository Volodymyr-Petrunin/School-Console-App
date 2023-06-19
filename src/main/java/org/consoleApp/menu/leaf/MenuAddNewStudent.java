package org.consoleApp.menu.leaf;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.menu.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuAddNewStudent implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
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
        System.out.println("Enter student details =)");
        System.out.print("First Name: ");
        String firstName = scan.next();

        System.out.print("Last Name: ");
        String lastName = scan.next();

        System.out.println("All groups: ");
        List<Group> allGroups = groupDAO.findAll();

        for (Group group : allGroups){
            System.out.println("Group name: " + group.getName());
        }

        System.out.print("Choose group name: ");
        String groupName = scan.next();

        Optional<Group> optionalGroup = groupDAO.findGroupIdByName(groupName);
        int groupId = optionalGroup.orElseThrow(()-> new RuntimeException("Can't find group id by group name in LaunchApp")).getId();

        Student newStudent = new Student(null,groupId,firstName,lastName);

        boolean operationSuccessful = studentsDAO.insert(newStudent);
        boolean enrollSuccessful = studentsDAO.enrollStudentInCourse(newStudent.getId(), groupId);

        if (operationSuccessful && enrollSuccessful){
            System.out.println("New student added successfully! :)");
            System.out.println(dash);
        }else {
            System.out.println("Something wrong! :(");
            System.out.println(dash);
        }
    }
}
