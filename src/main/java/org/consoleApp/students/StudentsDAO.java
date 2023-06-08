package org.consoleApp.students;

import java.util.List;

public interface StudentsDAO {
    List<Student> findAll();
    Student findById(int studentId);
    boolean insertNewStudent(Student student);
    void updateStudent(Student student);
    boolean deleteStudentById(int studentId);
    int getGroupSize(int groupId);
    int getNextStudentId();
    List<Student> findByFirstName(String firstName);
}
