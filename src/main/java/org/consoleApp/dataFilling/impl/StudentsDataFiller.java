package org.consoleApp.dataFilling.impl;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Group;
import org.consoleApp.generation.impl.GenerationDataInitial;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.InitialAmountGeneration;

import java.util.List;

public class StudentsDataFiller implements DataFiller {
    private StudentsDAO studentsDAO;
    private GenerationDataInitial dataInitial;

    public StudentsDataFiller(List<String> dataName, List<String> dataSurname, InitialAmountGeneration amountGeneration, StudentsDAO studentsDAO, GroupDAO groupsDAO) {
        this.studentsDAO = studentsDAO;
        this.dataInitial = new GenerationDataInitial(dataName, dataSurname, amountGeneration, groupsDAO, findAllGroups(groupsDAO));
    }

    @Override
    public void fillData() {
        List<Student> students = dataInitial.generateData();
        studentsDAO.insertBatch(students);
    }

    private List<Group> findAllGroups(GroupDAO groupDAO){
        List<Group> groups = groupDAO.findAll();

        if (groups.isEmpty()){
            throw new RuntimeException("Can't get all groups in StudentDataFiller");
        }

        return groups;
    }
}
