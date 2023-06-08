package org.consoleApp.enrollments;

import java.util.List;

public interface EnrollmentsDAO {
    boolean enrollStudentInCourse(int studentId, int courseId);
    boolean deleteStudentById(int studentId);
    List<Integer> findAllStudentsIdByCourseId(int courseId);
    List<Integer> findAllCourseIdByStudentsId(int studentId);
    boolean removeStudentFromCourse(int studentId, int courseId);
}
