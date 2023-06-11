package org.consoleApp.dataFilling.impl;


import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.GenerationDataInitial;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.domin.Student;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

public class StudentsDataFiller implements DataFiller {
    private final Random random = new Random();
    private List<String> dataName;
    private List<String> dataSurname;
    private int quantityGenerations;
    private int maxGroupSize;
    private StudentsDAOImpl studentsDAO;
    private GroupsDAOImpl groupsDAO;
    private GenerationDataInitial dataInitial;

    public StudentsDataFiller(List<String> dataName, List<String> dataSurname,int quantityGenerations, int maxGroupSize,DataSource dataSource) {
        this.dataName = dataName;
        this.dataSurname = dataSurname;
        this.quantityGenerations = quantityGenerations;
        this.maxGroupSize = maxGroupSize;
        this.studentsDAO = new StudentsDAOImpl(dataSource);
        this.groupsDAO = new GroupsDAOImpl(dataSource);
        this.dataInitial = new GenerationDataInitial(dataName, dataSurname,maxGroupSize,quantityGenerations,dataSource);
    }

    @Override
    public void fillData() {
        List<Student> students = dataInitial.generateData();
        studentsDAO.insertBatch(students);
    }
}
