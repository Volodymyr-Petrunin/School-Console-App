package org.consoleApp.dao.spring_jpa.adapters;

import jakarta.transaction.Transactional;
import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.spring_jpa.CourseRepositoryJPA;
import org.consoleApp.domin.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile("spring-data-jpa")
@Transactional
public class JPACourseRepositoryAdapter implements CourseDAO {
    private final CourseRepositoryJPA repositoryJPA;

    @Autowired
    public JPACourseRepositoryAdapter(CourseRepositoryJPA courseRepositoryJPA) {
        this.repositoryJPA = courseRepositoryJPA;
    }

    @Override
    public List<Course> findAll() {
        return repositoryJPA.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Optional<Course> findById(int id) {
        return repositoryJPA.findById(id);
    }

    @Override
    public boolean insert(Course course) {
        try {
            repositoryJPA.save(course);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't insert course", e);
        }
    }

    @Override
    public void insertBatch(List<Course> courses) {
        repositoryJPA.saveAll(courses);
    }

    @Override
    public boolean update(Course course) {
        try {
            repositoryJPA.save(course);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't update course", e);
        }
    }

    @Override
    public boolean delete(Course course) {
        try {
            repositoryJPA.delete(course);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete course", e);
        }
    }

    @Override
    public List<Course> findAllCourseByStudentsId(int studentId) {
        return repositoryJPA.findCoursesByStudentsId(studentId);
    }
}
