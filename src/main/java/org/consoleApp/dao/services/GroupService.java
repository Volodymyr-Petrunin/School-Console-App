package org.consoleApp.dao.services;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupDAO groupDAO;
    private final DataFiller groupDataFiller;

    @Autowired
    public GroupService(GroupDAO groupDAO, @Qualifier("groupDataFiller") DataFiller groupDataFiller) {
        this.groupDAO = groupDAO;
        this.groupDataFiller = groupDataFiller;
    }

    public List<Group> getAllGroups() {
        return groupDAO.findAll();
    }

    public Optional<Group> getGroupById(int groupId) {
        return groupDAO.findById(groupId);
    }

    public boolean createGroup(Group group) {
        return groupDAO.insert(group);
    }

    public void createMultipleGroups(List<Group> groups) {
        groupDAO.insertBatch(groups);
    }

    public boolean updateGroup(Group group) {
        return groupDAO.update(group);
    }

    public void deleteGroup(int groupId) {
       groupDAO.findById(groupId).ifPresent(groupDAO::delete);
    }

    public List<Group> getGroupsWithLessOrEqualStudents(int maxStudents) {
        return groupDAO.findGroupsWithLessOrEqualStudents(maxStudents);
    }

    public void generateDataAndPopulateDB(){
        groupDataFiller.fillData();
    }
}
