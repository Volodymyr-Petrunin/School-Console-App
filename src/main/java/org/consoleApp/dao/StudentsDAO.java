package org.consoleApp.dao;

import org.consoleApp.domin.Student;

import java.util.List;
import java.util.Optional;

public interface StudentsDAO {
    List<Student> findAll();
    Optional<Student> findById(int studentId);
    boolean insert(Student student);
    void updateStudent(Student student);
    boolean deleteStudentById(int studentId);
    int getGroupSize(int groupId);
    List<Student> findByFirstName(String firstName);
}
