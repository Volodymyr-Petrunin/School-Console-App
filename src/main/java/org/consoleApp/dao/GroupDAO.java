package org.consoleApp.dao;

import org.consoleApp.domin.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDAO extends GenericDAO<Group>{
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
}
