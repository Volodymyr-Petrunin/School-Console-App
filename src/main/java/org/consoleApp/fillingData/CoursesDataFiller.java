package org.consoleApp.fillingData;

import org.consoleApp.courses.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.GenerationTestData;
import org.consoleApp.readers.ResourcesFileReader;

import java.util.List;
import java.util.Random;

public class CoursesDataFiller implements DataFiller{
    private int quantityGenerations;
    private final Random random = new Random();
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final List<String> coursesList = readerCourses.read();
    private final GenerationTestData testData = new GenerationTestData();
    private final DBConnector dbConnector = new DBConnector();
    private CourseDAOImpl course = new CourseDAOImpl(dbConnector);

    public CoursesDataFiller(int quantityGenerations) {
        this.quantityGenerations = quantityGenerations;
    }
    @Override
    public void fillData() {
        for (int currentIndex = 0; currentIndex < quantityGenerations; currentIndex++){

        }
    }
}
