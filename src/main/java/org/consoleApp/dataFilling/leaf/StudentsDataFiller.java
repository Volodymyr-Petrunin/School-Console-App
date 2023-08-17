package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.StudentsGenerationData;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.impl.StudentsGeneratorService;

import java.util.List;

public class StudentsDataFiller implements DataFiller {
    private StudentsDAO studentsDAO;
    private StudentsGenerationData dataInitial;

    public StudentsDataFiller(StudentsGeneratorService studentsGeneratorService, StudentsDAO studentsDAO) {
        this.studentsDAO = studentsDAO;
        this.dataInitial = new StudentsGenerationData(studentsGeneratorService);
    }

    @Override
    public void fillData() {
        List<Student> students = dataInitial.generateData();
        studentsDAO.insertBatch(students);
    }
}
