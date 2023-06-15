package org.consoleApp.dataFilling.impl;


import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.GenerationDataInitial;
import org.consoleApp.domin.Student;

import java.util.List;

public class StudentsDataFiller implements DataFiller {
    private StudentsDAO studentsDAO;
    private GenerationDataInitial dataInitial;

    public StudentsDataFiller(List<String> dataName, List<String> dataSurname,int quantityGenerations, int maxGroupSize,StudentsDAO studentsDAO, GroupDAO groupsDAO) {
        this.studentsDAO = studentsDAO;
        this.dataInitial = new GenerationDataInitial(dataName, dataSurname,maxGroupSize,quantityGenerations,groupsDAO, groupsDAO.findAll());
    }

    @Override
    public void fillData() {
        List<Student> students = dataInitial.generateData();
        studentsDAO.insertBatch(students);
    }
}
