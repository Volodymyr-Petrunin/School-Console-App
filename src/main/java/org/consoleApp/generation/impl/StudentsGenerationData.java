package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;
import org.consoleApp.generation.records.InitialAmountGeneration;

import java.util.*;

public class GenerationDataInitial implements GenerationData<Student> {
    private final Random random = new Random();
    private List<String> dataName;
    private List<String> dataSurname;
    private int quantity;
    private int maxGroupSize;
    private List<Group> allGroups;

    public GenerationDataInitial(List<String> dataName, List<String> dataSurname, InitialAmountGeneration amountGeneration, List<Group> allGroups) {
        this.dataName = dataName;
        this.dataSurname = dataSurname;
        this.quantity = amountGeneration.quantityGenerations();
        this.maxGroupSize = amountGeneration.maxGroupSize();
        this.allGroups = allGroups;
    }

    @Override
    public List<Student> generateData(){
        List<Student> students = generateStudents();

        return assignStudentToGroup(students);
    }

    private List<Student> generateStudents(){
        List<Student> resultData = new ArrayList<>();

        for (int currentIndex = 0; currentIndex < quantity; currentIndex++){
            String currentName = getRandomElement(dataName);
            String currentSurname = getRandomElement(dataSurname);

            resultData.add(new Student(null,0, currentName, currentSurname));
        }

        return resultData;
    }

    private List<Student> assignStudentToGroup(List<Student> students){
        HashMap<Group, Integer> groupUsage = new HashMap<>();

        for (Student student : students) {
            Group randomGroup = getRandomElement(allGroups);

            student.setGroupId(randomGroup.getId());
            int currentUsage = groupUsage.getOrDefault(randomGroup, 0);
            groupUsage.put(randomGroup, currentUsage + 1);

            if (currentUsage + 1 >= maxGroupSize) {
                groupUsage.remove(randomGroup);
            }
        }

        return students;
    }

    private <T> T getRandomElement(List<T> list){
        int index = random.nextInt(list.size());
        return list.get(index);
    }
}
