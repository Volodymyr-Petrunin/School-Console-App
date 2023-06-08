package org.consoleApp.groups;

import java.util.List;

public interface GroupDAO {
    List<Group> findAll();
    Group findGroupById(int id);
    void insertNewGroup(Group group);
    void updateGroup(Group group);
    void deleteGroup(Group group);
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
    Group findGroupIdByName(String groupName);
}
