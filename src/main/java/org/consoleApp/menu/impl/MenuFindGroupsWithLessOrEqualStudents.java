package org.consoleApp.menu.impl;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;
import org.consoleApp.menu.MenuItem;

import java.util.List;
import java.util.Scanner;

public class MenuFindGroupsWithLessOrEqualStudents implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private GroupDAO groupsDAO;
    private String dash;

    public MenuFindGroupsWithLessOrEqualStudents(GroupDAO groupsDAO, String dash) {
        this.groupsDAO = groupsDAO;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Find all groups with less or equal number of students";
    }

    @Override
    public void execute() {
        System.out.println("Now write how many students should be in one group at least and I will try to find such groups :)");
        int maxStudentsInGroup = scan.nextInt();

        List<Group> allCourses = groupsDAO.findGroupsWithLessOrEqualStudents(maxStudentsInGroup);
        if (!allCourses.isEmpty()) {
            System.out.println("All Group:");
            for (Group group : allCourses) {
                System.out.println("Group name: " + group.getGroupName() + " and group id " + group.getGroupId());
            }
            System.out.println(dash);
        }else {
            System.out.println("No find groups!");
            System.out.println(dash);
        }
    }
}
