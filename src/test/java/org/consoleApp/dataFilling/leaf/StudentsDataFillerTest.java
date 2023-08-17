package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.domin.Group;
import org.consoleApp.generation.impl.StudentsGeneratorService;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsDataFillerTest{
    @Mock private StudentsDAOImpl studentsDAO;
    @Mock private StudentsGeneratorService studentsGeneratorService;
    private final List<String> names = List.of(
            "Vova", "Stas", "Dima"
    );
    private final List<String> surnames = List.of(
            "Petrunin", "Solyanik", "Dypai"
    );
    private StudentsDataFiller dataFiller;

    @BeforeEach
    void before(){
        dataFiller = new StudentsDataFiller(studentsGeneratorService, studentsDAO);
    }

    @Test
    void testFillData_ShouldInsertBatchOfStudentsInDB(){
        when(studentsGeneratorService.getNameList()).thenReturn(names);
        when(studentsGeneratorService.getSurnameList()).thenReturn(surnames);

        dataFiller.fillData();

        verify(studentsDAO, times(1)).insertBatch(anyList());
    }
}