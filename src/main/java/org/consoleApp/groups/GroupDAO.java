package org.consoleApp.groups;

import java.util.List;

public interface GroupDAO {
    List<Group> findAll();
    Group findById(int id);
    void insert(Group group);
    void update(Group group);
    void delete(Group group);
}
