package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;
import org.consoleApp.generation.records.InitialAmountGeneration;

import java.util.*;

public class StudentsGenerationData implements GenerationData<Student> {
    private final Random random = new Random();
    private List<String> dataName;
    private List<String> dataSurname;
    private int quantity;
    private int maxGroupSize;
    private int minGroupSize;
    private List<Group> allGroups;

    public StudentsGenerationData(List<String> dataName, List<String> dataSurname, InitialAmountGeneration amountGeneration, List<Group> allGroups) {
        this.dataName = dataName;
        this.dataSurname = dataSurname;
        this.quantity = amountGeneration.quantityGenerations();
        this.maxGroupSize = amountGeneration.maxGroupSize();
        this.minGroupSize = amountGeneration.minGroupSize();
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

            resultData.add(new Student(null,null, currentName, currentSurname));
        }

        return resultData;
    }

    private List<Student> assignStudentToGroup(List<Student> students){
        List<Student> studentList = new ArrayList<>(students);
        List<Student> groupStudents = new ArrayList<>();

        int remainingStudents = studentList.size();

        for (Group group : allGroups){
            int groupSize = randomRange(minGroupSize, maxGroupSize);

            for (int index = 0; index < groupSize; index++){
                if (studentList.isEmpty()){
                    break;
                }

                Student student = studentList.get(0);
                student.setGroupId(group.getId());
                groupStudents.add(student);

                studentList.remove(0);
                remainingStudents--;
            }

            if (remainingStudents < minGroupSize) {
                break;
            }
        }

        if (remainingStudents != 0){
            groupStudents.addAll(studentList);
        }

        return groupStudents;
    }

    private <T> T getRandomElement(List<T> list){
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private int randomRange(int minGroupSize, int maxGroupSize){
        return random.nextInt(maxGroupSize - minGroupSize) + minGroupSize;
    }
}
