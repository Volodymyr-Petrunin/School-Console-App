package org.consoleApp.students;

import java.util.List;

public interface StudentsDAO {
    List<Student> findAll();
    Student findById(int studentId);
    void insertNewStudent(Student student);
    void updateStudent(Student student);
    void deleteStudentById(int studentId);
    int getGroupSize(int groupId);
    int getNextStudentId();
}
