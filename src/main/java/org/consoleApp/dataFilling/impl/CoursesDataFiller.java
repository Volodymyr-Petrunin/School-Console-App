package org.consoleApp.dataFilling.impl;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Course;
import org.consoleApp.parser.impl.CourseParser;

import java.util.ArrayList;
import java.util.List;

public class CoursesDataFiller implements DataFiller {
    private final CourseParser parser = new CourseParser();
    private List<Course> coursesList;
    private CourseDAO courseDAO;

    public CoursesDataFiller(List<String> coursesList, CourseDAO courseDAO) {
        this.coursesList = parsedList(coursesList);
        this.courseDAO = courseDAO;
    }

    @Override
    public void fillData() {
        courseDAO.insertBatch(coursesList);
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
