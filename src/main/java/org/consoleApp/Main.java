package org.consoleApp;

import org.consoleApp.Courses.Course;
import org.consoleApp.Courses.CourseDAOImpl;
import org.consoleApp.DataBaseSettings.DBConnector;
import org.consoleApp.DataBaseSettings.ScriptRunner;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBConnector dbConnector = new DBConnector();
        String scriptFilePath = "src\\main\\resources\\SQLScript\\create_tables.sql";
        ScriptRunner scriptRunner = new ScriptRunner(dbConnector);
        scriptRunner.runScript(scriptFilePath);

        CourseDAOImpl courseDAO = new CourseDAOImpl(dbConnector);

        Course newCourse = new Course(1,"New Course", "Description");
        courseDAO.insert(newCourse);

        Course courseToUpdate = new Course(1, "Updated Course", "Updated Description");
        courseDAO.update(courseToUpdate);

        int courseId = 1;
        Course foundCourse = courseDAO.findById(courseId);
        System.out.println(foundCourse);

        Course courseToDelete = new Course(1,null, null);
        courseDAO.delete(courseToDelete);


        List<Course> allCourses = courseDAO.findAll();
        System.out.println("All Courses:");
        for (Course course : allCourses) {
            System.out.println(course);
        }
    }
}