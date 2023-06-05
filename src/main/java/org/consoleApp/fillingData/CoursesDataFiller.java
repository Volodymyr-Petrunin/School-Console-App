package org.consoleApp.fillingData;

import org.consoleApp.courses.Course;
import org.consoleApp.courses.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.parser.CourseParser;
import org.consoleApp.readers.ResourcesFileReader;
import org.consoleApp.records.CourseInfo;

import java.util.ArrayList;
import java.util.List;

public class CoursesDataFiller implements DataFiller{
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final CourseParser parser = new CourseParser();
    private final List<CourseInfo> coursesList = parsedList(readerCourses.read());
    private final List<String> courseNameList = courseName(coursesList);
    private final List<String> courseDescriptionList = courseDescription(coursesList);
    private final DBConnector dbConnector = new DBConnector();
    private CourseDAOImpl courseImpl = new CourseDAOImpl(dbConnector);

    @Override
    public void fillData() {
        if (courseNameList.size() == courseDescriptionList.size()) {
            for (int currentIndex = 0; currentIndex < courseNameList.size(); currentIndex++) {
                String name = courseNameList.get(currentIndex);
                String description = courseDescriptionList.get(currentIndex);

                Course course = new Course(currentIndex, name, description);
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

    private List<String> courseName(List<CourseInfo> currentObject){
        List<String> nameList = new ArrayList<>();
        for (CourseInfo currentLine : currentObject){
            nameList.add(currentLine.name());
        }
        return nameList;
    }
    private List<String> courseDescription(List<CourseInfo> currentObject){
        List<String> descriptionList = new ArrayList<>();
        for (CourseInfo currentLine : currentObject){
            descriptionList.add(currentLine.description());
        }
        return descriptionList;
    }
}
