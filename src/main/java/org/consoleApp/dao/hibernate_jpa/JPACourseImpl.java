package org.consoleApp.dao.hibernate_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.consoleApp.dao.CourseDAO;
import org.consoleApp.domin.Course;
import org.consoleApp.domin.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Profile("hibernate_jpa")
@Transactional
@Repository
public class JPACourseImpl implements CourseDAO {
    @Value("${courseBatchSize}")
    private int batchSize;
    private static final String FIND_ALL = "SELECT c FROM Course c ORDER BY c.id";
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JPACourseImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Course> findAll() {
        return entityManager.createQuery(FIND_ALL, Course.class).getResultList();
    }

    @Override
    public Optional<Course> findById(int id) {
        Course course = entityManager.find(Course.class, id);
        return Optional.ofNullable(course);
    }

    @Override
    public boolean insert(Course course) {
        try {
            entityManager.persist(course);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't insert course", e);
        }
    }

    @Override
    public void insertBatch(List<Course> courses) {
        int currentObj = 0;

        for (Course course : courses){
            entityManager.persist(course);

            if (currentObj % batchSize == 0 && currentObj > 0){
                entityManager.flush();
                entityManager.clear();
            }

            currentObj++;
        }

        entityManager.flush();
    }


    @Override
    public boolean update(Course course) {
        try {
            entityManager.merge(course);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't update course", e);
        }
    }

    @Override
    public boolean delete(Course course) {
        try {
            Course findCourse = entityManager.find(Course.class, course.getId());
            entityManager.remove(findCourse);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete course", e);
        }
    }

    @Override
    public List<Course> findAllCourseByStudentsId(int studentId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
        Root<Course> courseRoot = query.from(Course.class);
        Join<Course, Student> courseStudentJoin = courseRoot.join("students");

        query.select(courseRoot).where(criteriaBuilder.equal(courseStudentJoin.get("id"), studentId));

        return entityManager.createQuery(query).getResultList();
    }
}
