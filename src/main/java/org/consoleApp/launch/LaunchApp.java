package org.consoleApp.launch;

import jakarta.annotation.PostConstruct;
import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.dataFilling.composite.DataFillerComposite;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.dataFilling.leaf.CoursesDataFiller;
import org.consoleApp.dataFilling.leaf.EnrollmentsDataFiller;
import org.consoleApp.dataFilling.leaf.GroupDataFiller;
import org.consoleApp.dataFilling.leaf.StudentsDataFiller;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.composite.MenuComposite;
import org.consoleApp.menu.leaf.*;
import org.consoleApp.parser.impl.CourseParser;
import org.consoleApp.readers.ResourcesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;

@Service
public class LaunchApp {
    private final DBConnector dbConnector = new DBConnector();
    private final DataSource dataSource = dbConnector.getDBConnection();
    private final InputStream createTablesStream = getClass().getResourceAsStream("/SQLScript/create_tables.sql");
    private final ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final GroupAmountGeneration groupAmountGeneration = new GroupAmountGeneration(10, 2, 2);
    private final InitialAmountGeneration amountGeneration = new InitialAmountGeneration(200, 30, 10);
    private final CourseParser courseParser = new CourseParser();
    private final CourseDAO courseDAO;
    private final GroupDAO groupsDAO;
    private final StudentsDAO studentsDAO;
    private final GroupDataFiller groupDataFiller;
    private final StudentsDataFiller studentsDataFiller;
    private final CoursesDataFiller coursesDataFiller;
    private final EnrollmentsDataFiller enrollmentsDataFiller;
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything

    @Autowired
    public LaunchApp(CourseDAO courseDAO, GroupDAO groupsDAO, StudentsDAO studentsDAO) {
        this.courseDAO = courseDAO;
        this.groupsDAO = groupsDAO;
        this.studentsDAO = studentsDAO;
        this.groupDataFiller = new GroupDataFiller(groupAmountGeneration, groupsDAO);
        this.studentsDataFiller = new StudentsDataFiller(readerFirstName.read(), readerSecondName.read(), amountGeneration, studentsDAO, groupsDAO);
        this.coursesDataFiller = new CoursesDataFiller(courseParser.parsedList(readerCourses.read()), courseDAO);
        this.enrollmentsDataFiller = new EnrollmentsDataFiller(3, studentsDAO, courseDAO);
    }

    @PostConstruct
    public void launch(){
        scriptRunner.runScript(createTablesStream);

        List<DataFiller> dataFillers = List.of(
                coursesDataFiller, groupDataFiller, studentsDataFiller, enrollmentsDataFiller
        );

        List<MenuItem> menuItems = menuItems(courseDAO, groupsDAO, studentsDAO, dash);

        DataFillerComposite fillerComposite = new DataFillerComposite(dataFillers);

        MenuComposite menuComposite = new MenuComposite(menuItems, dash);

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