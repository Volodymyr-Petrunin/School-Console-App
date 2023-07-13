package org.consoleApp.parser.impl;

import org.consoleApp.domin.Course;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseParserTest {
    private final CourseParser courseParser = new CourseParser();

    @Test
    void testParser_ShouldParseString_AndReturnCourse(){
        String input = "Mathematics_This course about Math.";

        Course actual = courseParser.parse(input);
        Course expected = new Course(null, "Mathematics", "This course about Math.");

        assertEquals(expected, actual);
    }

    @Test
    void testParser_ShouldHandleIllegalArgumentException(){
        String input = "Wrong string :)";

        assertThrows(IllegalArgumentException.class,() -> courseParser.parse(input));
    }
}