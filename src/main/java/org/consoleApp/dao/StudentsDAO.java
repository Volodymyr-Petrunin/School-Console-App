package org.consoleApp.dao;

import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.EnrollInfo;

import java.util.List;

public interface StudentsDAO extends GenericDAO<Student> {
    boolean deleteByStudentId(int studentId);
    List<Student> findByFirstName(String firstName);
    boolean enrollStudentInCourse(int studentId, int courseId);
    void enrollBatchStudentInCourse(List<EnrollInfo> enrollInfo);
    boolean removeStudentFromCourse(int studentId, int courseId);
    List<Student> findStudentsByCourseName(String courseName);
}
