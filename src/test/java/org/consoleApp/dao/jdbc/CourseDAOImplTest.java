package org.consoleApp.dao.jdbc;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.parametrized.CourseBase;

class CourseDAOImplTest extends CourseBase {
    @Override
    protected CourseDAO createCourseBase() {
        return new CourseDAOImpl(dataSource);
    }
}