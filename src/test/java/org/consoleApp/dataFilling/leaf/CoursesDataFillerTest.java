package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.domin.Course;
import org.consoleApp.generation.impl.CoursesGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoursesDataFillerTest {
    @Mock private CourseDAOImpl courseDAO;
    @Mock private CoursesGeneratorService coursesGeneratorService;
    private final List<Course> expected = List.of(
            new Course(1, "PE", "PE"),
            new Course(2, "IT", "IT"),
            new Course(3, "Music", "Skryabin")
    );
    private CoursesDataFiller dataFiller;

    @BeforeEach
    void setup() {
        dataFiller = new CoursesDataFiller(coursesGeneratorService, courseDAO);
    }


    @Test
    void testFillData_ShouldUseCorrectLogic_(){
        when(coursesGeneratorService.generateData()).thenReturn(expected);
        dataFiller.fillData();
        verify(courseDAO, times(1)).insertBatch(expected);
    }
}