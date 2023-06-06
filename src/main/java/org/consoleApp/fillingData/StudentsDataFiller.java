package org.consoleApp.fillingData;

import org.consoleApp.generationData.GenerationTestData;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.readers.ResourcesFileReader;
import org.consoleApp.students.Student;
import org.consoleApp.students.StudentsDAOImpl;

import java.util.List;

public class StudentsDataFiller implements DataFiller{
    private int quantityGenerations;
    private final DBConnector dbConnector = new DBConnector();
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final GenerationTestData generationTestData = new GenerationTestData();
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dbConnector);
    public StudentsDataFiller(int quantityGenerations) {
        this.quantityGenerations = quantityGenerations;
    }

    @Override
    public void fillData() {
        List<String> firstNameList = generationTestData.generationData(readerFirstName.read(),quantityGenerations);
        List<String> secondNameList = generationTestData.generationData(readerSecondName.read(),quantityGenerations);

        for (int currentIndex = 0; currentIndex < quantityGenerations; currentIndex++){
            Student student = new Student(currentIndex,1,firstNameList.get(currentIndex),secondNameList.get(currentIndex));
            studentsDAO.insert(student);
        }
    }
}
