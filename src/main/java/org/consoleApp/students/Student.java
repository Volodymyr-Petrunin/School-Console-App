package org.consoleApp.students;

import org.consoleApp.enrollments.Enrollment;

import java.util.List;

public record Student(int student_id, int group_id, String first_name, String last_name, List<Enrollment> enrollments) {
}
