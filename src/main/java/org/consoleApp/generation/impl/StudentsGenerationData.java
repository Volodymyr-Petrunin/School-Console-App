package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;
import org.consoleApp.generation.records.InitialAmountGeneration;

import java.util.*;

public class StudentsGenerationData implements GenerationData<Student> {
    private final Random random = new Random();
    private List<String> firstName;
    private List<String> lastName;
    private int quantity;
    private int maxSize;
    private int minSize;
    private List<Group> allGroups;

    public StudentsGenerationData(List<String> firstName, List<String> lastName, InitialAmountGeneration amountGeneration, List<Group> allGroups) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.quantity = amountGeneration.quantityGenerations();
        this.maxSize = amountGeneration.maxGroupSize();
        this.minSize = amountGeneration.minGroupSize();
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
            String currentName = getRandomElement(firstName);
            String currentSurname = getRandomElement(lastName);

            resultData.add(new Student(null,null, currentName, currentSurname));
        }

        return resultData;
    }

    private List<Student> assignStudentToGroup(List<Student> students){
        List<Student> studentList = new LinkedList<>(students);
        List<Student> groupStudents = new ArrayList<>();

        for (Group group : allGroups){
            int groupSize = Math.min(randomRange(minSize, maxSize), studentList.size());

            for (int index = 0; index < groupSize; index++){
                Student student = studentList.remove(0);
                student.setGroupId(group.getId());
                groupStudents.add(student);
            }

            if (studentList.size() < minSize) {
                break;
            }
        }


        groupStudents.addAll(studentList);
        return groupStudents;
    }

    private <T> T getRandomElement(List<T> list){
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private int randomRange(int minSize, int maxSize){
        return random.nextInt(maxSize - minSize) + minSize;
    }
}
