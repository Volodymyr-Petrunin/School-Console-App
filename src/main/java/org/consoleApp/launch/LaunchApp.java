package org.consoleApp.launch;

import org.consoleApp.dataFilling.leaf.DataFillerOption;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.dao.jdbc.EnrollmentsDAO;
import org.consoleApp.dataFilling.impl.CoursesDataFiller;
import org.consoleApp.dataFilling.impl.EnrollmentsDataFiller;
import org.consoleApp.dataFilling.impl.GroupDataFiller;
import org.consoleApp.dataFilling.impl.StudentsDataFiller;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.consoleApp.menu.leaf.*;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.composite.MenuOption;
import org.consoleApp.parser.impl.CourseParser;
import org.consoleApp.readers.ResourcesFileReader;

import javax.sql.DataSource;
import java.util.*;

public class LaunchApp {
    private final DBConnector dbConnector = new DBConnector();
    private final DataSource dataSource = dbConnector.getDBConnection();
    private final String scriptCreateTables = "src\\main\\resources\\SQLScript\\create_tables.sql";
    private final ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final EnrollmentsDAO enrollmentsDAO = new EnrollmentsDAO(dataSource);
    private final CourseParser courseParser = new CourseParser();
    private final GroupAmountGeneration groupAmountGeneration = new GroupAmountGeneration(10, 2, 2);
    private final InitialAmountGeneration amountGeneration = new InitialAmountGeneration(200, 30);
    private final GroupDataFiller groupDataFiller = new GroupDataFiller(groupAmountGeneration, groupsDAO);
    private final StudentsDataFiller studentsDataFiller = new StudentsDataFiller(readerFirstName.read(), readerSecondName.read(), amountGeneration, studentsDAO, groupsDAO);
    private final CoursesDataFiller coursesDataFiller = new CoursesDataFiller(courseParser.parsedList(readerCourses.read()), courseDAO);
    private final EnrollmentsDataFiller enrollmentsDataFiller = new EnrollmentsDataFiller(3, studentsDAO, courseDAO);
    private final Scanner scan = new Scanner(System.in);
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything
    private final List<MenuItem> menuItems = createMenuItems();
    private final MenuOption menuOption = new MenuOption(menuItems, dash);
    private final DataFillerOption fillerOption = new DataFillerOption(coursesDataFiller, groupDataFiller, studentsDataFiller, enrollmentsDataFiller);
    private boolean exit = false;
    public void launch(){
        scriptRunner.runScript(scriptCreateTables);
        fillerOption.fillData();

        while (!exit){
            menuOption.execute();
            userChooses();
        }
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
        menu.add(new MenuFindAllStudentsRelatedToCourse(courseDAO, enrollmentsDAO, studentsDAO, groupsDAO, dash));
        menu.add(new MenuAddNewStudent(groupsDAO, studentsDAO, dash));
        menu.add(new MenuDeleteStudent(enrollmentsDAO, studentsDAO, dash));
        menu.add(new MenuAddStudentToCourse(studentsDAO, courseDAO, groupsDAO, dash));
        menu.add(new MenuRemoveStudentFromOneOfCourses(studentsDAO, enrollmentsDAO, courseDAO, groupsDAO, dash));

        return menu;
    }
}