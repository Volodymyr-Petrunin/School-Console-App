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
import org.consoleApp.menu.composite.MenuOption;
import org.consoleApp.parser.impl.CourseParser;
import org.consoleApp.readers.ResourcesFileReader;

import javax.sql.DataSource;

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
    private final InitialAmountGeneration amountGeneration = new InitialAmountGeneration(200, 30, 10);
    private final GroupDataFiller groupDataFiller = new GroupDataFiller(groupAmountGeneration, groupsDAO);
    private final StudentsDataFiller studentsDataFiller = new StudentsDataFiller(readerFirstName.read(), readerSecondName.read(), amountGeneration, studentsDAO, groupsDAO);
    private final CoursesDataFiller coursesDataFiller = new CoursesDataFiller(courseParser.parsedList(readerCourses.read()), courseDAO);
    private final EnrollmentsDataFiller enrollmentsDataFiller = new EnrollmentsDataFiller(3, studentsDAO, courseDAO);
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything
    private final DataFillerOption fillerOption = new DataFillerOption(coursesDataFiller, groupDataFiller, studentsDataFiller, enrollmentsDataFiller);
    private final MenuOption menuOption = new MenuOption(groupsDAO, courseDAO, studentsDAO, enrollmentsDAO, dash);

    public void launch(){
        scriptRunner.runScript(scriptCreateTables);
        fillerOption.fillData();

        menuOption.execute();
    }
}