package org.consoleApp.dao;

import org.consoleApp.domin.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDAO {
    List<Group> findAll();
    Optional<Group> findById(int id);
    void insert(Group group);
    void insertBatch(List<Group> groups);
    void updateGroup(Group group);
    void deleteGroup(Group group);
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
    Optional<Group> findGroupIdByName(String groupName);
}
