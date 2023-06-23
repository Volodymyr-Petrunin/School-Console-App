package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Group;
import org.consoleApp.generation.impl.StudentsGenerationData;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.InitialAmountGeneration;

import java.util.List;

public class StudentsDataFiller implements DataFiller {
    private StudentsDAO studentsDAO;
    private List<Group> groups;
    private StudentsGenerationData dataInitial;

    public StudentsDataFiller(List<String> dataName, List<String> dataSurname, InitialAmountGeneration amountGeneration, StudentsDAO studentsDAO, GroupDAO groupsDAO) {
        this.studentsDAO = studentsDAO;
        this.groups = findAllGroups(groupsDAO);
        this.dataInitial = new StudentsGenerationData(dataName, dataSurname, amountGeneration, groups);
    }

    @Override
    public void fillData() {
        List<Student> students = dataInitial.generateData();
        studentsDAO.insertBatch(students);
    }

    private List<Group> findAllGroups(GroupDAO groupDAO){
        List<Group> groups = groupDAO.findAll();

        if (groups.isEmpty()){
            throw new IllegalStateException("Can't get all groups in StudentDataFiller");
        }

        return groups;
    }
}
