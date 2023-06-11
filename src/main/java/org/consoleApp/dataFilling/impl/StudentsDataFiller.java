package org.consoleApp.dataFilling.impl;


import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.GenerationDataInitial;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.domin.Group;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.readers.ResourcesFileReader;
import org.consoleApp.domin.Student;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentsDataFiller implements DataFiller {
    private int quantityGenerations;
    private final Random random = new Random();
    private final DBConnector dbConnector = new DBConnector(10);
    private final DataSource dataSource = dbConnector.getConnection();
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dataSource);
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    public StudentsDataFiller(int quantityGenerations) {
        this.quantityGenerations = quantityGenerations;
    }

    @Override
    public void fillData() {
        GenerationDataInitial firstName = new GenerationDataInitial(readerFirstName.read(),quantityGenerations);
        GenerationDataInitial secondName = new GenerationDataInitial(readerSecondName.read(),quantityGenerations);

        List<String> firstNameList = firstName.generateData();
        List<String> secondNameList = secondName.generateData();

        for (int currentIndex = 0; currentIndex < quantityGenerations; currentIndex++){
            int randomGroup = choseGroup();
            int nextStudentId = studentsDAO.getNextId();
            Student student = new Student(nextStudentId,randomGroup,firstNameList.get(currentIndex),secondNameList.get(currentIndex));
            studentsDAO.insert(student);
        }
    }

    private int choseGroup() {
        List<Group> allGroups = groupsDAO.findAll();
        List<Group> eligibleGroups = new ArrayList<>();

        for (Group group : allGroups){
            int groupSize = studentsDAO.getGroupSize(group.groupId());
            if (groupSize <= 30){
                eligibleGroups.add(group);
            }
        }

        if (eligibleGroups.isEmpty()){
            throw new RuntimeException("No eligible groups found with the required number of students.");
        }

        int groupIndex = random.nextInt(eligibleGroups.size());
        Group group = eligibleGroups.get(groupIndex);
        return group.groupId();
    }
}
