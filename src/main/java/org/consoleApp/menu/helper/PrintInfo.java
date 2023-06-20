package org.consoleApp.menu.helper;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

public class PrintInfo {

    public void printStudents(List<Student> students, GroupDAO groupDAO) {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        int maxFirstNameLength = findMaxNameLength(students, Student::getFirstName);
        int maxLastNameLength = findMaxNameLength(students, Student::getLastName);

        for (Student student : students) {
            Optional<Integer> groupIdOptional = student.getGroupId();

            String groupName = groupIdOptional.flatMap(groupDAO::findById)
                    .map(Group::getName)
                    .orElse("No group");

            result.add(String.format("ID: %3d Initial: %-" + maxFirstNameLength + "s %-" + maxLastNameLength + "s | Group: %s", student.getId(), student.getFirstName(), student.getLastName(), groupName));
        }

        System.out.println(result);
    }

    public void printCourses(List<Course> courses){
        StringJoiner result = new StringJoiner(System.lineSeparator());
        int maxCourseNameLength = findMaxNameLength(courses, Course::getName);
        int maxCourseDescriptionLength = findMaxNameLength(courses, Course::getDescription);

        for (Course course : courses){
            result.add(String.format("ID: %d Course name: %-" + maxCourseNameLength + "s Course description: %-" + maxCourseDescriptionLength + "s", course.getId(), course.getName(), course.getDescription()));
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
