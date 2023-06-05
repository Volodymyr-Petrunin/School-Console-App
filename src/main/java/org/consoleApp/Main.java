package org.consoleApp;

import org.consoleApp.DataBaseSettings.DBConnector;
import org.consoleApp.DataBaseSettings.ScriptRunner;
import org.consoleApp.Groups.Group;
import org.consoleApp.Groups.GroupsDAOImpl;
import org.consoleApp.Students.Student;
import org.consoleApp.Students.StudentsDAOImpl;
import org.consoleApp.readers.ResourcesFileReader;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        CourseDAOImpl courseDAO = new CourseDAOImpl(dbConnector);
//
//        Course newCourse = new Course(1,"New Course", "Description");
//        courseDAO.insert(newCourse);
//
//        Course course1 = new Course(1,"New Course", "Description");
//        courseDAO.insert(course1);
//
//        Course courseToUpdate = new Course(1, "Updated Course", "Updated Description");
//        courseDAO.update(courseToUpdate);
//
//        int courseId = 1;
//        Course foundCourse = courseDAO.findById(courseId);
//        System.out.println(foundCourse);
//
//
//
//        List<Course> allCourses = courseDAO.findAll();
//        System.out.println("All Courses:");
//        for (Course course : allCourses) {
//            System.out.println(course);
//        }

//        StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dbConnector);
//        GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dbConnector);
//
//        Group newGroup = new Group(1,"New Group");
//        groupsDAO.insert(newGroup);
//        System.out.println("Новая группа создана: " + newGroup);
//
//        System.out.println("Группа с ID 1: " + groupsDAO.findById(1));
//
//
//        // Обновляем данные группы
//        Group groupNew = new Group(1, "Update group");
//        groupsDAO.update(groupNew);
//        System.out.println("Данные группы обновлены: " + groupNew);
//
//        List<Group> groups = groupsDAO.findAll();
//        System.out.println("Список групп:");
//        for (Group groupList : groups) {
//            System.out.println(groupList);
//        }
//
//        // Удаляем группу
//        Group groupForDel = new Group(2,"for del");
//        groupsDAO.delete(groupForDel);
//        System.out.println("Группа удалена: " + groupForDel);
//
//        // Создаем нового студента
//        Student newStudent = new Student(1, 1,"John", "Doe");
//        studentsDAO.insert(newStudent);
//        System.out.println("Новый студент создан: " + newStudent);
//
//        // Получаем студента по ID
//        Student student = studentsDAO.findById(1);
//        System.out.println("Студент с ID 1: " + student);
//
//        // Получаем список всех студентов
//        List<Student> students = studentsDAO.findAll();
//        System.out.println("Список студентов:");
//        for (Student currentStudent : students) {
//            System.out.println(currentStudent);
//        }
//
//
//
//        // Обновляем данные студента
//        Student update = new Student(1,1,"Smith","lox");
//        studentsDAO.update(newStudent);
//        System.out.println("Данные студента обновлены: " + newStudent);
//
//        // Удаляем студента
//        studentsDAO.delete(newStudent.student_id());
//        System.out.println("Студент удален: " + newStudent);
//
//        // Закрываем соединение с базой данных
//        try {
//            dbConnector.closeConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

//        ResourcesFileReader readerFirstName = new ResourcesFileReader("courses.txt");
//        List<String> lol = readerFirstName.read();
        GenerationTestData generationTestData = new GenerationTestData();
//        System.out.println(generationTestData.generationData(lol,10));

//        System.out.println(generationTestData.generationGroups(10));

//        LaunchApp launchApp = new LaunchApp();
//        launchApp.launch();

    }
}