package org.consoleApp.dao.hibernate_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.EnrollInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Profile("hibernate_jpa")
@Transactional
@Repository
public class JPAStudentImpl implements StudentsDAO {
    @Value("${studentBatchSize}")
    private int BATCH_SIZE;
    private static final String FIND_ALL = "SELECT s FROM Student s ORDER BY s.id";

    @PersistenceContext
    private EntityManager entityManager;

    public JPAStudentImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Student> findAll() {
        return entityManager.createQuery(FIND_ALL, Student.class).getResultList();
    }

    @Override
    public Optional<Student> findById(int id) {
        Student student = entityManager.find(Student.class, id);
        return Optional.ofNullable(student);
    }

    @Override
    public boolean insert(Student student) {
        try {
            entityManager.persist(student);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't insert student", e);
        }
    }

    @Override
    public void insertBatch(List<Student> students) {
        int currentObj = 0;

        for (Student student : students){

            entityManager.persist(student);

            if (currentObj % BATCH_SIZE == 0 && currentObj > 0){
                entityManager.flush();
                entityManager.clear();
            }

            currentObj++;
        }

        entityManager.flush();
    }

    @Override
    public boolean update(Student student) {
        try {
            entityManager.merge(student);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't update student", e);
        }
    }

    @Override
    public boolean delete(Student student) {
        try {
            Student mergedStudent = entityManager.merge(student);
            entityManager.remove(mergedStudent);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete student", e);
        }
    }

    @Override
    public boolean deleteByStudentId(int studentId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Student> queryDelete = criteriaBuilder.createCriteriaDelete(Student.class);
        Root<Student> studentRoot = queryDelete.from(Student.class);

        queryDelete.where(criteriaBuilder.equal(studentRoot.get("id"), studentId));

        return entityManager.createQuery(queryDelete).executeUpdate() > 0;
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> query = criteriaBuilder.createQuery(Student.class);
        Root<Student> studentRoot = query.from(Student.class);

        query.select(studentRoot).where(criteriaBuilder.equal(studentRoot.get("firstName"), firstName));


        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        try {
            Student student = entityManager.find(Student.class, studentId);
            Course course = entityManager.find(Course.class, courseId);

            student.setCourses(Collections.singletonList(course));
            entityManager.merge(course);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't enroll student in course", e);
        }
    }

    @Override
    public void enrollBatchStudentInCourse(List<EnrollInfo> enrollInfo) {
        Student student;
        Course course;

        for (EnrollInfo currentInfo : enrollInfo){
            student = entityManager.find(Student.class, currentInfo.studentId());
            course = entityManager.find(Course.class, currentInfo.courseId());

            student.getCourses().add(course);
            entityManager.merge(course);
        }

    }

    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        try {
            Student student = entityManager.find(Student.class, studentId);
            Course course = entityManager.find(Course.class, courseId);

            student.getCourses().remove(course);
            entityManager.merge(student);

            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't remove student from course", e);
        }
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> query = criteriaBuilder.createQuery(Student.class);
        Root<Student> studentRoot = query.from(Student.class);

        Subquery<Course> subquery = query.subquery(Course.class);
        Root<Course> courseRoot = subquery.from(Course.class);

        subquery.select(courseRoot).where(criteriaBuilder.equal(courseRoot.get("name"), courseName));

        studentRoot.join("courses");
        query.where(criteriaBuilder.exists(subquery));

        return entityManager.createQuery(query).getResultList();
    }
}
