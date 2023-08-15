package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Course;
import org.consoleApp.generation.impl.CoursesGeneratorService;

import java.util.List;

public class CoursesDataFiller implements DataFiller {
    private CoursesGeneratorService coursesList;
    private CourseDAO courseDAO;

    public CoursesDataFiller(CoursesGeneratorService coursesList, CourseDAO courseDAO) {
        this.coursesList = coursesList;
        this.courseDAO = courseDAO;
    }

    @Override
    public void fillData() {
        courseDAO.insertBatch(coursesList.generateData());
    }
}
