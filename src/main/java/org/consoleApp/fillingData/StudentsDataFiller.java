package org.consoleApp.fillingData;

import org.consoleApp.courses.Course;
import org.consoleApp.courses.CourseDAOImpl;
import org.consoleApp.generationData.GenerationDataInitial;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.groups.Group;
import org.consoleApp.groups.GroupsDAOImpl;
import org.consoleApp.readers.ResourcesFileReader;
import org.consoleApp.students.Student;
import org.consoleApp.students.StudentsDAOImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentsDataFiller implements DataFiller{
    private int quantityGenerations;
    private final Random random = new Random();
    private final DBConnector dbConnector = new DBConnector();
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dbConnector);
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dbConnector);
    private final CourseDAOImpl courseDAO = new CourseDAOImpl(dbConnector);
    public StudentsDataFiller(int quantityGenerations) {
        this.quantityGenerations = quantityGenerations;
    }

    @Override
    public void fillData() {
        GenerationDataInitial firstName = new GenerationDataInitial(readerFirstName.read(),quantityGenerations);
        GenerationDataInitial secondName = new GenerationDataInitial(readerSecondName.read(),quantityGenerations);

        List<String> firstNameList = firstName.generationData();
        List<String> secondNameList = secondName.generationData();

        for (int currentIndex = 0; currentIndex < quantityGenerations; currentIndex++){
            int randomGroup = choseGroup();
            Student student = new Student(currentIndex,randomGroup,firstNameList.get(currentIndex),secondNameList.get(currentIndex),new ArrayList<>());
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
