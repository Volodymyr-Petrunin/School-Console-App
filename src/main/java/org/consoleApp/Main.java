package org.consoleApp;

import org.consoleApp.courses.Course;
import org.consoleApp.courses.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.fillingData.CoursesDataFiller;
import org.consoleApp.groups.Group;
import org.consoleApp.groups.GroupsDAOImpl;
import org.consoleApp.students.Student;
import org.consoleApp.students.StudentsDAOImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBConnector dbConnector = new DBConnector();
        CourseDAOImpl courseDAO = new CourseDAOImpl(dbConnector);

        StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dbConnector);
        GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dbConnector);

        List<Group> groups = groupsDAO.findAll();
        System.out.println("Список групп:");
        for (Group groupList : groups) {
            System.out.println(groupList);
        }


        // Получаем список всех студентов
        List<Student> students = studentsDAO.findAll();
        System.out.println("Список студентов:");
        for (Student currentStudent : students) {
            System.out.println(currentStudent);
        }


        CoursesDataFiller dataFiller = new CoursesDataFiller();
        dataFiller.fillData();

        List<Course> allCourses = courseDAO.findAll();
        System.out.println("All Courses:");
        for (Course course : allCourses) {
            System.out.println(course);
        }

        LaunchApp launchApp = new LaunchApp();
        launchApp.launch();

    }
}