package org.consoleApp.courses;

import org.consoleApp.enrollments.Enrollment;

import java.util.List;

public record Course(int courseId, String courseName, String courseDescription, List<Enrollment> enrollments) {
}
