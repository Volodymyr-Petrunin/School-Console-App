package org.consoleApp.dao.hibernate_jdbc;

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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Profile("hibernate_jdbc")
@Transactional
@Repository
@PropertySource("classpath:JPAImpl.properties")
public class JPACourseImpl implements CourseDAO {
    @Value("${courseBatchSize}")
    private int BATCH_SIZE;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JPACourseImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Course> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
        Root<Course> courseRoot = query.from(Course.class);

        query.select(courseRoot).orderBy(criteriaBuilder.asc(courseRoot.get("id")));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<Course> findById(int id) {
        Course course = entityManager.find(Course.class, id);
        return Optional.ofNullable(course);
    }

    @Override
    public boolean insert(Course course) {
        try {
            Course managedCourse = entityManager.merge(course);
            entityManager.persist(managedCourse);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void insertBatch(List<Course> courses) {
        int currentObj = 0;

        for (Course course : courses){
            Course managedCourse = entityManager.merge(course);
            entityManager.persist(managedCourse);

            if (currentObj % BATCH_SIZE == 0 && currentObj > 0){
                entityManager.flush();
                entityManager.clear();
            }

            currentObj++;
        }

        entityManager.flush();
    }


    @Override
    public boolean update(Course course) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Course> updateQuery = criteriaBuilder.createCriteriaUpdate(Course.class);
        Root<Course> courseRoot = updateQuery.from(Course.class);

        updateQuery.set("name", course.getName());
        updateQuery.set("description", course.getDescription());
        updateQuery.where(criteriaBuilder.equal(courseRoot.get("id"), course.getId()));

        return entityManager.createQuery(updateQuery).executeUpdate() > 0;
    }

    @Override
    public boolean delete(Course course) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Course> deleteQuery = criteriaBuilder.createCriteriaDelete(Course.class);
        Root<Course> courseRoot = deleteQuery.from(Course.class);

        deleteQuery.where(criteriaBuilder.equal(courseRoot.get("id"), course.getId()));

        return entityManager.createQuery(deleteQuery).executeUpdate() > 0;
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
