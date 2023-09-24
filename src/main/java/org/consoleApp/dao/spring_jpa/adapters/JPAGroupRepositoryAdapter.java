package org.consoleApp.dao.spring_jpa.adapters;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.spring_jpa.GroupRepositoryJPA;
import org.consoleApp.domin.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile("spring-data-jpa")
public class JPAGroupRepositoryAdapter implements GroupDAO {
    private final GroupRepositoryJPA repositoryJPA;

    @Autowired
    public JPAGroupRepositoryAdapter(GroupRepositoryJPA repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }

    @Override
    public List<Group> findAll() {
        return repositoryJPA.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Optional<Group> findById(int id) {
        return repositoryJPA.findById(id);
    }

    @Override
    public boolean insert(Group group) {
        try {
            repositoryJPA.save(group);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't save group", e);
        }
    }

    @Override
    public void insertBatch(List<Group> groups) {
        repositoryJPA.saveAll(groups);
    }

    @Override
    public boolean update(Group group) {
        try {
            repositoryJPA.save(group);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't save group", e);
        }
    }

    @Override
    public boolean delete(Group group) {
        try {
            repositoryJPA.delete(group);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete group", e);
        }
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        return repositoryJPA.findGroupsWithLessOrEqualStudents(maxStudents);
    }
}
