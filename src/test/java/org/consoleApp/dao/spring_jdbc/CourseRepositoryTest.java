package org.consoleApp.dao.spring_jdbc;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CleanupAndFillData;
import org.consoleApp.dao.parametrized.CourseBase;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseRepositoryTest extends CourseBase {

    @Override
    protected CourseDAO createCourseBase() {
        return new CourseRepository(dataSource);
    }
}