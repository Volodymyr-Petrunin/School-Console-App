package org.consoleApp.dataFilling.impl;

import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Course;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.parser.impl.CourseParser;
import org.consoleApp.records.CourseInfo;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CoursesDataFiller implements DataFiller {
    private final CourseParser parser = new CourseParser();
    private List<CourseInfo> coursesList;
    private CourseDAOImpl courseImpl;

    public CoursesDataFiller(List<String> coursesList, DataSource dataSource) {
        this.coursesList = parsedList(coursesList);
        this.courseImpl = new CourseDAOImpl(dataSource);
    }

    @Override
    public void fillData() {
        List<String> courseNameList = getCourseInfo(coursesList, CourseInfo::name);
        List<String> courseDescriptionList = getCourseInfo(coursesList, CourseInfo::description);

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
