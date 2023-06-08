package org.consoleApp.enrollments;

import java.util.List;

public interface EnrollmentsDAO {
    void enrollStudentInCourse(int studentId, int courseId);
    void deleteStudentById(int studentId);
    List<Integer> findAllStudentsIdByCourseId(int courseId);
}
