package org.consoleApp.fillingData;

import org.consoleApp.generationData.GenerationDataInitial;
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
    private final StudentsDAOImpl studentsDAO = new StudentsDAOImpl(dbConnector);
    public StudentsDataFiller(int quantityGenerations) {
        this.quantityGenerations = quantityGenerations;
    }

    @Override
    public void fillData() {
        GenerationDataInitial firstName = new GenerationDataInitial(readerFirstName.read(),quantityGenerations);
        GenerationDataInitial secondName = new GenerationDataInitial(readerSecondName.read(),quantityGenerations);

        List<String> firstNameList = firstName.generationData();
        List<String> secondNameList = secondName.generationData();

        System.out.println(firstNameList);
        System.out.println(secondNameList);

        for (int currentIndex = 0; currentIndex < quantityGenerations; currentIndex++){
            Student student = new Student(currentIndex,1,firstNameList.get(currentIndex),secondNameList.get(currentIndex));
            studentsDAO.insert(student);
        }
    }
}
