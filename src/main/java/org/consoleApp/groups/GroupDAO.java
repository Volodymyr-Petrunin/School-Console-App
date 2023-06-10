package org.consoleApp.groups;

import java.util.List;

public interface GroupDAO {
    List<Group> findAll();
    Group findById(int id);
    void insert(Group group);
    void updateGroup(Group group);
    void deleteGroup(Group group);
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
    Group findGroupIdByName(String groupName);
}
