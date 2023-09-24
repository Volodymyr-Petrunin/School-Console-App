package org.consoleApp.dao.spring_jpa;

import org.consoleApp.domin.Course;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("spring-data-jpa")
public interface CourseRepositoryJPA extends JpaRepository<Course, Integer> {
    List<Course> findCoursesByStudentsId(int studentId);
}
