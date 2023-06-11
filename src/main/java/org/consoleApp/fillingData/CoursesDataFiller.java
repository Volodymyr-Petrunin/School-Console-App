package org.consoleApp.fillingData;

import org.consoleApp.courses.Course;
import org.consoleApp.courses.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.parser.CourseParser;
import org.consoleApp.readers.ResourcesFileReader;
import org.consoleApp.records.CourseInfo;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CoursesDataFiller implements DataFiller{
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final CourseParser parser = new CourseParser();
    private final List<CourseInfo> coursesList = parsedList(readerCourses.read());
    private final List<String> courseNameList = getCourseInfo(coursesList, CourseInfo::name);
    private final List<String> courseDescriptionList = getCourseInfo(coursesList, CourseInfo::description);
    private final DBConnector dbConnector = new DBConnector(10);
    private final DataSource dataSource = dbConnector.getConnection();
    private final CourseDAOImpl courseImpl = new CourseDAOImpl(dataSource);

    @Override
    public void fillData() {
        if (courseNameList.size() == courseDescriptionList.size()) {
            for (int currentIndex = 0; currentIndex < courseNameList.size(); currentIndex++) {
                String name = courseNameList.get(currentIndex);
                String description = courseDescriptionList.get(currentIndex);
                int nextCourseId = courseImpl.getNextId();

                Course course = new Course(nextCourseId, name, description);
                courseImpl.insert(course);
            }
        }
    }

    private List<CourseInfo> parsedList(List<String> list){
        List<CourseInfo> result = new ArrayList<>();
        for (String currentLine : list){
            CourseInfo currentObj = parser.parse(currentLine);
            result.add(new CourseInfo(currentObj.name(),currentObj.description()));
        }
        return result;
    }

    private List<String> getCourseInfo(List<CourseInfo> currentObject, Function<CourseInfo, String> getInfo){
        List<String> currentList = new ArrayList<>();
        for (CourseInfo currentLine : currentObject){
            String value = getInfo.apply(currentLine);
            currentList.add(value);
        }
        return currentList;
    }
}
