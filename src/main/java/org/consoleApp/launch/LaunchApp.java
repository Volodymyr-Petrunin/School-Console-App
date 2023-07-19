package org.consoleApp.launch;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.spring_jdbc.CourseRepository;
import org.consoleApp.dao.spring_jdbc.GroupRepository;
import org.consoleApp.dao.spring_jdbc.StudentsRepository;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.dataFilling.composite.DataFillerComposite;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.dataFilling.leaf.CoursesDataFiller;
import org.consoleApp.dataFilling.leaf.EnrollmentsDataFiller;
import org.consoleApp.dataFilling.leaf.GroupDataFiller;
import org.consoleApp.dataFilling.leaf.StudentsDataFiller;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.composite.MenuComposite;
import org.consoleApp.menu.composite.MenuSwapToSpringJDBC;
import org.consoleApp.menu.leaf.*;
import org.consoleApp.parser.impl.CourseParser;
import org.consoleApp.readers.ResourcesFileReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;

public class LaunchApp {
    private final DBConnector dbConnector = new DBConnector();
    private final DataSource dataSource = dbConnector.getDBConnection();
    private final InputStream createTablesStream = getClass().getResourceAsStream("/SQLScript/create_tables.sql");
    private final ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
    private final CourseRepository courseRepository = new CourseRepository(dataSource);
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final GroupRepository groupRepository = new GroupRepository(dataSource);
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final StudentsRepository studentsRepository = new StudentsRepository(dataSource);
    private final CourseParser courseParser = new CourseParser();
    private final GroupAmountGeneration groupAmountGeneration = new GroupAmountGeneration(10, 2, 2);
    private final InitialAmountGeneration amountGeneration = new InitialAmountGeneration(200, 30, 10);
    private final GroupDataFiller groupDataFiller = new GroupDataFiller(groupAmountGeneration, groupsDAO);
    private final StudentsDataFiller studentsDataFiller = new StudentsDataFiller(readerFirstName.read(), readerSecondName.read(), amountGeneration, studentsDAO, groupsDAO);
    private final CoursesDataFiller coursesDataFiller = new CoursesDataFiller(courseParser.parsedList(readerCourses.read()), courseDAO);
    private final EnrollmentsDataFiller enrollmentsDataFiller = new EnrollmentsDataFiller(3, studentsDAO, courseDAO);
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything

    public void launch(){
        scriptRunner.runScript(createTablesStream);

        List<DataFiller> dataFillers = List.of(
                coursesDataFiller, groupDataFiller, studentsDataFiller, enrollmentsDataFiller
        );

        List<MenuItem> menuItems = menuItems(courseDAO, groupsDAO, studentsDAO, dash);
        List<MenuItem> springMenuItems = menuItems(courseRepository, groupRepository, studentsRepository, dash);

        DataFillerComposite fillerComposite = new DataFillerComposite(dataFillers);

        MenuSwapToSpringJDBC toSpringJDBC = new MenuSwapToSpringJDBC(springMenuItems, dash);
        MenuComposite menuComposite = new MenuComposite(menuItems, toSpringJDBC, dash);

        fillerComposite.fillData();

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