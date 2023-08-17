package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
@PropertySource("classpath:studentsDataFillerConfiguration.properties")
public class StudentsGenerationData implements GenerationData<Student> {
    private final Random random = new Random();
    private final StudentsGeneratorService studentsGeneratorService;

    @Value("${initialQuantityGenerations}") private int quantity;
    @Value("${maxGroupSize}") private int maxSize;
    @Value("${minGroupSize}")private int minSize;

    @Autowired
    public StudentsGenerationData(StudentsGeneratorService generatorService) {
        this.studentsGeneratorService = generatorService;
    }

    @Override
    public List<Student> generateData(){
        List<Student> students = generateStudents();

        return assignStudentToGroup(students);
    }

    private List<Student> generateStudents(){
        List<String> firstName = studentsGeneratorService.getNameList();
        List<String> lastName = studentsGeneratorService.getSurnameList();

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

        List<Group> allGroups = studentsGeneratorService.getGroupList();

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
