package org.consoleApp.dao;

import org.consoleApp.domin.Student;

import java.util.List;

public interface StudentsDAO extends GenericDAO<Student> {
    boolean deleteByStudentId(int studentId);
    List<Student> findByFirstName(String firstName);
    boolean enrollStudentInCourse(int studentId, int courseId);
    boolean deleteStudentByIdFromEnrollments(int studentId);
    List<Integer> findAllStudentsIdByCourseId(int courseId);
    boolean removeStudentFromCourse(int studentId, int courseId);
}
