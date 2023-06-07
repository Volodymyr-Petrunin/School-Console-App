package org.consoleApp.students;

import java.util.List;

public interface StudentsDAO {
    List<Student> findAll();
    Student findById(int studentId);
    void insert(Student student);
    void update(Student student);
    void deleteStudentById(int studentId);
    int getGroupSize(int groupId);
}
