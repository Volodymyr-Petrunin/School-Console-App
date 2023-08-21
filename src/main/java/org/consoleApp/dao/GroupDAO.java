package org.consoleApp.dao;

import org.consoleApp.domin.Group;

import java.util.List;

public interface GroupDAO extends GenericDAO<Group>{
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
}
