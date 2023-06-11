package org.consoleApp.launch;

import org.consoleApp.domin.Course;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataBaseSettings.ScriptRunner;
import org.consoleApp.dao.jdbc.EnrollmentsDAOImpl;
import org.consoleApp.dataFilling.impl.CoursesDataFiller;
import org.consoleApp.dataFilling.impl.EnrollmentsDataFiller;
import org.consoleApp.dataFilling.impl.GroupDataFiller;
import org.consoleApp.dataFilling.impl.StudentsDataFiller;
import org.consoleApp.domin.Group;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.domin.Student;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.readers.ResourcesFileReader;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Function;

public class LaunchApp {
    private final DBConnector dbConnector = new DBConnector(70);
    private final DataSource dataSource = dbConnector.getConnection();
    private final String scriptCreateTables = "src\\main\\resources\\SQLScript\\create_tables.sql";
    private final ScriptRunner scriptRunner = new ScriptRunner(dataSource);
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dataSource);
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final EnrollmentsDAOImpl enrollmentsDAO = new EnrollmentsDAOImpl(dataSource);
    private final GroupDataFiller groupDataFiller = new GroupDataFiller(10,2,2, dataSource);
    private final StudentsDataFiller studentsDataFiller = new StudentsDataFiller(200, 30,dataSource);
    private final CoursesDataFiller coursesDataFiller = new CoursesDataFiller(readerCourses.read(),dataSource);
    private final EnrollmentsDataFiller enrollmentsDataFiller = new EnrollmentsDataFiller(3, dataSource);
    private final Scanner scan = new Scanner(System.in);
    private final String dash = "-".repeat(50); // yes magic number. But it doesn't affect anything
    private boolean exit = false;
    public void launch(){

        while (!exit){
            System.out.println(menu());
            userChooses();
        }
    }

    private String menu(){
        StringJoiner menu = new StringJoiner(System.lineSeparator());

        menu.add("Please select an option:");
        menu.add("1. Find all groups with less or equal number of students");
        menu.add("2. Find all students related to a course with a specified name");
        menu.add("3. Add a new student");
        menu.add("4. Delete a student by STUDENT_ID");
        menu.add("5. Add a student to a course");
        menu.add("6. Remove a student from one of their courses");
        menu.add("");
        menu.add("0. Exit");
        menu.add(dash);

        return menu.toString();
    }

    private void userChooses(){
        System.out.print("Yor choice: ");
        int currentChose = scan.nextInt();

        if (currentChose == 1){
            findGroupsWithLessOrEqualStudents();
        }else if (currentChose == 2){
            findAllStudentsRelatedToCourseWithSpecifiedName();
        }else if (currentChose == 3){
            addNewStudent();
        }else if (currentChose == 4){
            deleteStudentByStudentId();
        }else if (currentChose == 5){
            addStudentToCourse();
        }else if (currentChose == 6){
            removeStudentFromOneOfTheirCourses();
        } else {
            exit = true;
        }
    }

    public void fillData(){
        scriptRunner.runScript(scriptCreateTables);

        coursesDataFiller.fillData();
        groupDataFiller.fillData();
        studentsDataFiller.fillData();
        enrollmentsDataFiller.fillData();
    }

    private void findGroupsWithLessOrEqualStudents(){
        System.out.println("Now write how many students should be in one group at least and I will try to find such groups :)");
        int maxStudentsInGroup = scan.nextInt();

        List<Group> allCourses = groupsDAO.findGroupsWithLessOrEqualStudents(maxStudentsInGroup);
        if (!allCourses.isEmpty()) {
            System.out.println("All Group:");
            for (Group group : allCourses) {
                System.out.println("Group name: " + group.groupName() + " and group id " + group.groupId());
            }
            System.out.println(dash);
        }else {
            System.out.println("No find groups!");
            System.out.println(dash);
        }
    }

    private void deleteStudentByStudentId(){
        System.out.println("Now write the id of the student you wont to delete ;)");
        int studentId = scan.nextInt();

        if (studentId != 0){
           boolean deleteFromEnrollSuccessful = enrollmentsDAO.deleteStudentById(studentId);
           boolean deleteFromStudentsSuccessful = studentsDAO.deleteStudentById(studentId);

           if (deleteFromEnrollSuccessful && deleteFromStudentsSuccessful){
               System.out.println("Delete student successfully!");
               System.out.println(dash);
           }else {
               System.out.println("Something wrong! :(");
               System.out.println(dash);
           }
        }
    }
    private void findAllStudentsRelatedToCourseWithSpecifiedName(){
        System.out.println("Now write the name of course 0_0");
        String courseName = scan.next();
        Optional<Course> optionalCourse = courseDAO.findByCourseName(courseName);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<Integer> studentsId = enrollmentsDAO.findAllStudentsIdByCourseId(course.getCourseId());

            List<Student> students = new ArrayList<>();

            for (Integer currentId : studentsId) {
                studentsDAO.findById(currentId).ifPresent(students::add);
            }

            System.out.println("All Students: ");
            printStudents(students);

            System.out.println(dash);
        } else {
            System.out.println("Wrong course name :(");
        }
    }

    private void addNewStudent(){
        System.out.println("Enter student details =)");
        System.out.print("First Name: ");
        String firstName = scan.next();

        System.out.print("Last Name: ");
        String lastName = scan.next();

        System.out.println("All groups: ");
        List<Group> allGroups = groupsDAO.findAll();

        for (Group group : allGroups){
            System.out.println("Group name: " + group.groupName());
        }

        System.out.print("Choose group name: ");
        String groupName = scan.next();

        Optional<Group> optionalGroup = groupsDAO.findGroupIdByName(groupName);
        int groupId = optionalGroup.orElseThrow(()-> new RuntimeException("Can't find group id by group name in LaunchApp")).groupId();

        int studentId = studentsDAO.getNextId();

        Student newStudent = new Student(studentId,groupId,firstName,lastName);
        boolean operationSuccessful = studentsDAO.insert(newStudent);

        if (operationSuccessful){
            System.out.println("New student added successfully! :)");
            System.out.println(dash);
        }else {
            System.out.println("Something wrong! :(");
            System.out.println(dash);
        }
    }

    private void removeStudentFromOneOfTheirCourses(){
        System.out.print("Enter student name: ");
        String studentName = scan.next();

        List<Student> foundedStudents = studentsDAO.findByFirstName(studentName);

        System.out.println("I find " + foundedStudents.size() + " students:");

        printStudents(foundedStudents);

        if (!foundedStudents.isEmpty()) {
            System.out.print("Now choose which one you need and write its id: ");
            int studentId = scan.nextInt();

            List<Integer> coursesId = enrollmentsDAO.findAllCourseIdByStudentsId(studentId);
            List<Course> courses = new ArrayList<>();

            for (Integer currentInt : coursesId) {
                courseDAO.findById(currentInt).ifPresent(courses::add);
            }

            System.out.println("Now select the course id you want to remove from your student: ");

            printCourses(courses);

            int courseId = scan.nextInt();

            boolean deleteFromCourseSuccessful = enrollmentsDAO.removeStudentFromCourse(studentId, courseId);

            if (deleteFromCourseSuccessful) {
                System.out.println("Delete student successfully!");
                System.out.println(dash);
            } else {
                System.out.println("Something wrong! :(");
                System.out.println(dash);
            }
        }
    }

    private void addStudentToCourse(){
        List<Student> students = studentsDAO.findAll();
        List<Course> courses = courseDAO.findAll();

        System.out.println("All students:");
        printStudents(students);

        System.out.print("Now choose student id: ");
        int studentId = scan.nextInt();

        System.out.println("All courses:");
        printCourses(courses);

        System.out.print("Now choose courses id: ");
        int coursesId = scan.nextInt();

        boolean addStudentSuccess = studentsDAO.enrollStudentInCourse(studentId, coursesId);

        if (addStudentSuccess){
            System.out.println("Add student successfully!");
            System.out.println(dash);
        }else {
            System.out.println("Something wrong! :(");
            System.out.println(dash);
        }
    }

    private void printStudents(List<Student> students) {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        int maxFirstNameLength = findMaxNameLength(students, Student::first_name);
        int maxLastNameLength = findMaxNameLength(students, Student::last_name);

        for (Student student : students){
            Optional<Group> optionalGroup = groupsDAO.findById(student.group_id());

            if (optionalGroup.isPresent()) {
                Group group = optionalGroup.get();
                result.add(String.format("ID: %3d Initial: %-" + maxFirstNameLength + "s %-" + maxLastNameLength + "s | Group: %s", student.student_id(), student.first_name(), student.last_name(), group.groupName()));
            }
        }

        System.out.println(result);
    }

    private void printCourses(List<Course> courses){
        StringJoiner result = new StringJoiner(System.lineSeparator());
        int maxCourseNameLength = findMaxNameLength(courses, Course::getCourseName);
        int maxCourseDescriptionLength = findMaxNameLength(courses, Course::getCourseDescription);

        for (Course course : courses){
            result.add(String.format("ID: %d Course name: %-" + maxCourseNameLength + "s Course description: %-" + maxCourseDescriptionLength + "s", course.getCourseId(), course.getCourseName(), course.getCourseDescription()));
        }

        System.out.println(result);
    }

    private <T> int findMaxNameLength(List<T> objects, Function<T, String> nameExtractor) {
        int maxLength = 0;

        for (T object : objects) {
            int nameLength = nameExtractor.apply(object).length();
            maxLength = Math.max(maxLength, nameLength);
        }

        return maxLength;
    }
}