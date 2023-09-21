package org.consoleApp.dao.hibernate_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;
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
public class JPAGroupImpl implements GroupDAO {

    @Value("${groupBatchSize}")
    private int batchSize;
    private static final String FIND_ALL = "SELECT g FROM Group g ORDER BY g.id";
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JPAGroupImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Group> findAll() {
        return entityManager.createQuery(FIND_ALL, Group.class).getResultList();
    }

    @Override
    public Optional<Group> findById(int id) {
        Group group = entityManager.find(Group.class, id);
        return Optional.ofNullable(group);
    }

    @Override
    public boolean insert(Group group) {
        try {
            entityManager.persist(group);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't insert group", e);
        }
    }

    @Override
    public void insertBatch(List<Group> groups) {
        int currentObj = 0;

        for (Group group : groups){
            entityManager.persist(group);

            if (currentObj % batchSize == 0 && currentObj > 0){
                entityManager.flush();
                entityManager.clear();
            }

            currentObj++;
        }

        entityManager.flush();
    }

    @Override
    public boolean update(Group group) {
        try {
            entityManager.merge(group);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't update group", e);
        }
    }

    @Override
    public boolean delete(Group group) {
        try {
            Group mergedGroup = entityManager.merge(group);
            entityManager.remove(mergedGroup);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete group", e);
        }
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Group> query = criteriaBuilder.createQuery(Group.class);
        Root<Group> groupRoot = query.from(Group.class);

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Student> studentRoot = subquery.from(Student.class);

        subquery.select(criteriaBuilder.count(studentRoot.get("id")))
                .where(criteriaBuilder.equal(studentRoot.get("group"), groupRoot));

        query.select(groupRoot).where(criteriaBuilder.le(subquery, (long) maxStudents));

        return entityManager.createQuery(query).getResultList();
    }
}
