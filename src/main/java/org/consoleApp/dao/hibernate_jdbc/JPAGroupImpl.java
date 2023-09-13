package org.consoleApp.dao.hibernate_jdbc;

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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Profile("hibernate_jdbc")
@Transactional
@Repository
@PropertySource("classpath:JPAImpl.properties")
public class JPAGroupImpl implements GroupDAO {

    @Value("${groupBatchSize}")
    private int BATCH_SIZE;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JPAGroupImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Group> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
        Root<Group> root = criteriaQuery.from(Group.class);

        criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get("id")));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<Group> findById(int id) {
        Group group = entityManager.find(Group.class, id);
        return Optional.ofNullable(group);
    }

    @Override
    public boolean insert(Group group) {
        try {
            Group managedGroup = entityManager.merge(group);
            entityManager.persist(managedGroup);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void insertBatch(List<Group> groups) {
        int currentObj = 0;

        for (Group group : groups){
            Group managedGroup = entityManager.merge(group);
            entityManager.persist(managedGroup);

            if (currentObj % BATCH_SIZE == 0 && currentObj > 0){
                entityManager.flush();
                entityManager.clear();
            }

            currentObj++;
        }

        entityManager.flush();
    }

    @Override
    public boolean update(Group group) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Group> updateQuery = criteriaBuilder.createCriteriaUpdate(Group.class);
        Root<Group> groupRoot = updateQuery.from(Group.class);

        updateQuery.set("name", group.getName());
        updateQuery.where(criteriaBuilder.equal(groupRoot.get("id"), group.getId()));

        return entityManager.createQuery(updateQuery).executeUpdate() > 0;
    }

    @Override
    public boolean delete(Group group) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Group> deleteQuery = criteriaBuilder.createCriteriaDelete(Group.class);
        Root<Group> groupRoot = deleteQuery.from(Group.class);

        deleteQuery.where(criteriaBuilder.equal(groupRoot.get("id"), group.getId()));

        return entityManager.createQuery(deleteQuery).executeUpdate() > 0;
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
