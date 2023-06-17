package org.consoleApp.dataFilling.impl;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Course;

import java.util.List;

public class CoursesDataFiller implements DataFiller {
    private List<Course> coursesList;
    private CourseDAO courseDAO;

    public CoursesDataFiller(List<Course> coursesList, CourseDAO courseDAO) {
        this.coursesList = coursesList;
        this.courseDAO = courseDAO;
    }

    @Override
    public void fillData() {
        courseDAO.insertBatch(coursesList);
    }
}
