package org.consoleApp.dao.spring_jpa.adapters;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.spring_jpa.StudentRepositoryJPA;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.EnrollInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Profile("spring-data-jpa")
@Transactional
public class JPAStudentRepositoryAdapter implements StudentsDAO {

    private final StudentRepositoryJPA repositoryJPA;

    @Autowired
    public JPAStudentRepositoryAdapter(StudentRepositoryJPA repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }

    @Override
    public List<Student> findAll() {
        return repositoryJPA.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Optional<Student> findById(int id) {
        return repositoryJPA.findById(id);
    }

    @Override
    public boolean insert(Student student) {
        try {
            repositoryJPA.save(student);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't insert student", e);
        }
    }

    @Override
    public void insertBatch(List<Student> students) {
        repositoryJPA.saveAll(students);
    }

    @Override
    public boolean update(Student student) {
        try {
            repositoryJPA.save(student);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't update student", e);
        }
    }

    @Override
    public boolean delete(Student student) {
        try {
            repositoryJPA.delete(student);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete student", e);
        }
    }

    @Override
    public boolean deleteByStudentId(int studentId) {
        try {
            repositoryJPA.deleteById(studentId);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete student by id", e);
        }
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        return repositoryJPA.findAllByFirstName(firstName);
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        if (repositoryJPA.existsById(studentId)){
            Student student = repositoryJPA.findFirstById(studentId);
            Course course = repositoryJPA.findCourseById(courseId);

            student.addCourse(course);
            repositoryJPA.save(student);
            return true;
        }

        return false;
    }

    @Override
    public void enrollBatchStudentInCourse(List<EnrollInfo> enrollInfo) {
        for (EnrollInfo currentInfo : enrollInfo){
            enrollStudentInCourse(currentInfo.studentId(), currentInfo.courseId());
        }
    }

    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        if (repositoryJPA.existsById(studentId)){
            Student student = repositoryJPA.findFirstById(studentId);
            Course course = repositoryJPA.findCourseById(courseId);

            student.getCourses().remove(course);
            repositoryJPA.save(student);
            return true;
        }

        throw new IllegalStateException("Can't remove student from course");
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        return repositoryJPA.findStudentsByCourseName(courseName);
    }
}
