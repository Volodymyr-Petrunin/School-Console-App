package org.consoleApp.dataFilling.impl;

import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Course;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.parser.impl.CourseParser;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class CoursesDataFiller implements DataFiller {
    private CourseParser parser;
    private List<Course> coursesList;
    private CourseDAOImpl courseImpl;

    public CoursesDataFiller(List<String> coursesList, DataSource dataSource) {
        this.parser = new CourseParser(dataSource);
        this.coursesList = parsedList(coursesList);
        this.courseImpl = new CourseDAOImpl(dataSource);
    }

    @Override
    public void fillData() {
        courseImpl.insertBatch(coursesList);
    }

    private List<Course> parsedList(List<String> list) {
        List<Course> result = new ArrayList<>();
        for (String currentLine : list) {
            Course course = parser.parse(currentLine);
            result.add(course);
        }
        return result;
    }
}
