package org.consoleApp.Students;

import java.util.List;

public interface StudentsDAO {
    List<Student> findById();
    Student findById(int studentId);
    void insert(Student student);
    void update(Student student);
    void delete(int studentId);
}
