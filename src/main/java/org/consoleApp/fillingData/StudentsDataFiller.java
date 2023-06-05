package org.consoleApp.fillingData;

import org.consoleApp.readers.ResourcesFileReader;

import java.util.List;
import java.util.Random;

public class StudentsDataFiller implements DataFiller{
    private final Random random = new Random();
    private final ResourcesFileReader readerCourses = new ResourcesFileReader("courses.txt");
    private final ResourcesFileReader readerFirstName = new ResourcesFileReader("firstName.txt");
    private final ResourcesFileReader readerSecondName = new ResourcesFileReader("secondName.txt");
    private final List<String> coursesList = readerCourses.read();
    private final List<String> firstNameList = readerFirstName.read();
    private final List<String> secondNameList = readerSecondName.read();

    @Override
    public void fillData() {

    }
}
