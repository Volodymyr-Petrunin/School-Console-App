package org.consoleApp.generation.impl;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;
import org.consoleApp.generation.records.InitialAmountGeneration;

import java.util.*;

public class GenerationDataInitial implements GenerationData<Student> {
    private final Random random = new Random();
    private List<String> dataName;
    private List<String> dataSurname;
    private GroupDAO groupDAO;
    private int quantity;
    private int maxGroupSize;
    private List<Group> allGroups;

    public GenerationDataInitial(List<String> dataName, List<String> dataSurname, InitialAmountGeneration amountGeneration, GroupDAO groupDAO, List<Group> allGroups) {
        this.dataName = dataName;
        this.dataSurname = dataSurname;
        this.quantity = amountGeneration.quantityGenerations();
        this.maxGroupSize = amountGeneration.maxGroupSize();
        this.groupDAO = groupDAO;
        this.allGroups = allGroups;
    }

    @Override
    public List<Student> generateData(){
        List<Student> resultData = new ArrayList<>();

        for (int currentIndex = 0; currentIndex < quantity; currentIndex++){
            String currentName = getRandomElement(dataName);
            String currentSurname = getRandomElement(dataSurname);

            int groupId = choseGroup();

            resultData.add(new Student(null,groupId, currentName, currentSurname));
        }

        return resultData;
    }

    private <T> T getRandomElement(List<T> list){
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private int choseGroup() {
        List<Group> eligibleGroups = new ArrayList<>();

        for (Group group : allGroups){
            int groupSize = groupDAO.getGroupSize(group.getId());
            if (groupSize <= maxGroupSize){
                eligibleGroups.add(group);
            }
        }

        if (eligibleGroups.isEmpty()){
            throw new RuntimeException("No eligible groups found with the required number of students.");
        }

        int groupIndex = random.nextInt(eligibleGroups.size());
        Group group = eligibleGroups.get(groupIndex);
        return group.getId();
    }
}
