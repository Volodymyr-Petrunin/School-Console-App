package org.consoleApp.dao.spring_jpa;

import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("spring-data-jpa")
public interface StudentRepositoryJPA extends JpaRepository<Student, Integer> {
    List<Student> findAllByFirstName(String name);

    Student findFirstById(Integer id);

    @Query("select s from Student s inner join s.courses courses where courses.name = ?1")
    List<Student> findStudentsByCourseName(String name);

    @Query("select c from Course c where c.id = :id")
    Course findCourseById(int id);
}
