package org.consoleApp.generation.impl;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;

import javax.sql.DataSource;
import java.util.*;

public class GenerationDataInitial implements GenerationData<Student> {
    private final Random random = new Random();
    private List<String> dataName;
    private List<String> dataSurname;
    private GroupsDAOImpl groupsDAO;
    private StudentsDAOImpl studentsDAO;
    private int quantity;
    private int maxGroupSize;

    public GenerationDataInitial(List<String> dataName, List<String> dataSurname, int maxGroupSize,int quantity, DataSource dataSource) {
        this.dataName = dataName;
        this.dataSurname = dataSurname;
        this.quantity = quantity;
        this.maxGroupSize = maxGroupSize;
        this.groupsDAO = new GroupsDAOImpl(dataSource);
        this.studentsDAO = new StudentsDAOImpl(dataSource);
    }

    @Override
    public List<Student> generateData(){
        List<Student> resultData = new ArrayList<>();

        for (int currentIndex = 0; currentIndex < quantity; currentIndex++){
            String currentName = getRandomElement(dataName);
            String currentSurname = getRandomElement(dataSurname);

            int studentId = studentsDAO.getNextId();
            int groupId = choseGroup();

            resultData.add(new Student(studentId,groupId, currentName, currentSurname));
        }

        return resultData;
    }

    private <T> T getRandomElement(List<T> list){
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private int choseGroup() {
        List<Group> allGroups = groupsDAO.findAll();
        List<Group> eligibleGroups = new ArrayList<>();

        for (Group group : allGroups){
            int groupSize = studentsDAO.getGroupSize(group.getGroupId());
            if (groupSize <= maxGroupSize){
                eligibleGroups.add(group);
            }
        }

        if (eligibleGroups.isEmpty()){
            throw new RuntimeException("No eligible groups found with the required number of students.");
        }

        int groupIndex = random.nextInt(eligibleGroups.size());
        Group group = eligibleGroups.get(groupIndex);
        return group.getGroupId();
    }
}
