package org.consoleApp.dao;

import java.util.List;

public interface EnrollmentsDAO {
    boolean deleteStudentById(int studentId);
    List<Integer> findAllStudentsIdByCourseId(int courseId);
    List<Integer> findAllCourseIdByStudentsId(int studentId);
    boolean removeStudentFromCourse(int studentId, int courseId);
}
