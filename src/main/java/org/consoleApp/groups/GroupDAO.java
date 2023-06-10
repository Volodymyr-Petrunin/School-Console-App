package org.consoleApp.groups;

import java.util.List;
import java.util.Optional;

public interface GroupDAO {
    List<Group> findAll();
    Optional<Group> findById(int id);
    void insert(Group group);
    void updateGroup(Group group);
    void deleteGroup(Group group);
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
    Optional<Group> findGroupIdByName(String groupName);
}
