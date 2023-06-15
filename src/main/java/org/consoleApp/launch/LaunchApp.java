package org.consoleApp.launch;

import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.dao.jdbc.Enrollments;
import org.consoleApp.dataFilling.impl.CoursesDataFiller;
import org.consoleApp.dataFilling.impl.EnrollmentsDataFiller;
import org.consoleApp.dataFilling.impl.GroupDataFiller;
import org.consoleApp.dataFilling.impl.StudentsDataFiller;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.menu.impl.*;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.readers.ResourcesFileReader;

import javax.sql.DataSource;
import java.util.*;

public class LaunchApp {
    private final DBConnector dbConnector = new DBConnector(70);
    private final DataSource dataSource = dbConnector.getConnection();
    private final String scriptCreateTables = "src\\main\\resources\\SQLScript\\create_tables.sql";
    private final ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final Enrollments enrollmentsDAO = new Enrollments(dataSource);
    private final GroupDataFiller groupDataFiller = new GroupDataFiller(10,2,2, groupsDAO);
    private final StudentsDataFiller studentsDataFiller = new StudentsDataFiller(readerFirstName.read(),readerSecondName.read(),200, 30, studentsDAO, groupsDAO);
    private final CoursesDataFiller coursesDataFiller = new CoursesDataFiller(readerCourses.read(), courseDAO);
    private final EnrollmentsDataFiller enrollmentsDataFiller = new EnrollmentsDataFiller(3, studentsDAO, courseDAO);
    private final Scanner scan = new Scanner(System.in);
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything
    private MenuItem menuItem;
    private boolean exit = false;
    public void launch(){

        while (!exit){
            System.out.println(menu());
            userChooses();
        }
    }

    private String menu(){
        StringJoiner menu = new StringJoiner(System.lineSeparator());

        menu.add("Please select an option: ");

        List<MenuItem> menuItems = createMenuItems();

        int place = 1;
        for (MenuItem item : menuItems){
            menu.add(place++ + ". " + item.getDescription());
        }

        menu.add("");

        menu.add("0. Exit");
        menu.add(dash);

        return menu.toString();
    }

    private void userChooses(){
        System.out.print("Yor choice: ");
        int currentChoice = scan.nextInt();

        List<MenuItem> menuItems = createMenuItems();

        if (currentChoice >= 1 && currentChoice <= menuItems.size()) {
            menuItems.get(currentChoice - 1).execute();
        } else if (currentChoice == 0) {
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

    private List<MenuItem> createMenuItems(){
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuFindGroupsWithLessOrEqualStudents(groupsDAO, dash));
        menuItems.add(new MenuFindAllStudentsRelatedToCourse(courseDAO, enrollmentsDAO, studentsDAO, groupsDAO, dash));
        menuItems.add(new MenuAddNewStudent(groupsDAO, studentsDAO, dash));
        menuItems.add(new MenuDeleteStudent(enrollmentsDAO, studentsDAO, dash));
        menuItems.add(new MenuAddStudentToCourse(studentsDAO, courseDAO, groupsDAO, dash));
        menuItems.add(new MenuRemoveStudentFromOneOfCourses(studentsDAO, enrollmentsDAO, courseDAO, groupsDAO, dash));

        return menuItems;
    }
}