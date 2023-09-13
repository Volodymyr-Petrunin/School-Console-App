package org.consoleApp.dao.hibernate_jdbc;

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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Profile("hibernate_jdbc")
@Transactional
@Repository
@PropertySource("classpath:JPAImpl.properties")
public class JPAStudentImpl implements StudentsDAO {
    @Value("${studentBatchSize}")
    private int BATCH_SIZE;
    private static final String REMOVE_STUDENT_FROM_COURSE = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
    private static final String FIND_STUDENT_BY_COURSE_NAME = "SELECT s.student_id, s.group_id, s.first_name, s.last_name FROM students s " +
            "JOIN enrollments e ON s.student_id = e.student_id JOIN courses c ON e.course_id = c.course_id " +
            "WHERE c.course_name = ?";

    @PersistenceContext
    private EntityManager entityManager;

    public JPAStudentImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Student> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> query = criteriaBuilder.createQuery(Student.class);
        Root<Student> studentRoot = query.from(Student.class);

        query.select(studentRoot).orderBy(criteriaBuilder.asc(studentRoot.get("id")));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<Student> findById(int id) {
        Student student = entityManager.find(Student.class, id);
        return Optional.ofNullable(student);
    }

    @Override
    public boolean insert(Student student) {
        try {
            Student managedStudent = entityManager.merge(student);
            entityManager.persist(managedStudent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void insertBatch(List<Student> students) {
        int currentObj = 0;

        for (Student student : students){
            Student managedStudent = entityManager.merge(student);
            entityManager.persist(managedStudent);

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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Student> queryUpdate = criteriaBuilder.createCriteriaUpdate(Student.class);
        Root<Student> studentRoot = queryUpdate.from(Student.class);

        Optional<Integer> groupIdOptional = student.getGroupId();
        Integer groupId = groupIdOptional.orElse(null);

        queryUpdate.set("groupId", groupId);
        queryUpdate.set("firstName", student.getFirstName());
        queryUpdate.set("lastName", student.getLastName());

        queryUpdate.where(criteriaBuilder.equal(studentRoot.get("id"), student.getId()));

        return entityManager.createQuery(queryUpdate).executeUpdate() > 0;
    }

    @Override
    public boolean delete(Student student) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Student> deleteQuery = criteriaBuilder.createCriteriaDelete(Student.class);
        Root<Student> groupRoot = deleteQuery.from(Student.class);

        deleteQuery.where(criteriaBuilder.equal(groupRoot.get("id"), student.getId()));

        return entityManager.createQuery(deleteQuery).executeUpdate() > 0;
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
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void enrollBatchStudentInCourse(List<EnrollInfo> enrollInfo) {
        List<Course> courses = new ArrayList<>();
        Student student = new Student();
        Course course = new Course();

        for (EnrollInfo currentInfo : enrollInfo){
            student = entityManager.find(Student.class, currentInfo.studentId());
            course = entityManager.find(Course.class, currentInfo.courseId());

            courses.add(course);
        }

        student.setCourses(courses);

        entityManager.merge(course);
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
