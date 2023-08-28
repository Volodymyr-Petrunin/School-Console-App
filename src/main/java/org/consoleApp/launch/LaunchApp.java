package org.consoleApp.launch;

import jakarta.annotation.PostConstruct;
import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.composite.DataServiceComposite;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.composite.MenuComposite;
import org.consoleApp.menu.leaf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaunchApp {
    private final CourseDAO courseDAO;
    private final GroupDAO groupsDAO;
    private final StudentsDAO studentsDAO;
    private final DataServiceComposite serviceComposite;
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything

    @Autowired
    public LaunchApp(CourseDAO courseDAO, GroupDAO groupsDAO, StudentsDAO studentsDAO, DataServiceComposite serviceComposite) {
        this.courseDAO = courseDAO;
        this.groupsDAO = groupsDAO;
        this.studentsDAO = studentsDAO;
        this.serviceComposite = serviceComposite;
    }

    @PostConstruct
    public void launch(){

        List<MenuItem> menuItems = menuItems(courseDAO, groupsDAO, studentsDAO, dash);

        MenuComposite menuComposite = new MenuComposite(menuItems, dash);

        serviceComposite.generateDataAndPopulateDB();

        menuComposite.execute();
    }

    private List<MenuItem> menuItems(CourseDAO courseDAO, GroupDAO groupDAO, StudentsDAO studentsDAO, String dash){
        return List.of(
                new MenuFindGroupsWithLessOrEqualStudents(groupDAO, dash),
                new MenuFindAllStudentsRelatedToCourse(studentsDAO, groupDAO, dash),
                new MenuAddNewStudent(groupDAO, studentsDAO, dash),
                new MenuDeleteStudent(studentsDAO, dash),
                new MenuAddStudentToCourse(studentsDAO, courseDAO, groupDAO, dash),
                new MenuRemoveStudentFromOneOfCourses(studentsDAO, courseDAO, groupDAO, dash)
        );
    }
}