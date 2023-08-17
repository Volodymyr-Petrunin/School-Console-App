package org.consoleApp.services;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;
import org.consoleApp.generation.GenerationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService implements ServicesDAOImpl {
    private final GroupDAO groupDAO;
    private final GenerationData<Group> groupGenerationData;

    @Autowired
    public GroupService(GroupDAO groupDAO, @Qualifier("groupGenerationData") GenerationData<Group> groupGenerationData) {
        this.groupDAO = groupDAO;
        this.groupGenerationData = groupGenerationData;
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
       createMultipleGroups(groupGenerationData.generateData());
    }
}
