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
    private final static int QUANTITY_GENERATION = 3;
    @Mock private StudentsDAOImpl studentsDAO;
    @Mock private GroupsDAOImpl groupsDAO;
    @Mock private StudentsGeneratorService studentsGeneratorService;
    private final List<Group> expectedGroup = List.of(
            new Group(1, "AA-11"),
            new Group(2, "BB-22"),
            new Group(3, "CC-33")
    );
    private final List<String> names = List.of(
            "Vova", "Stas", "Dima"
    );
    private final List<String> surnames = List.of(
            "Petrunin", "Solyanik", "Dypai"
    );
    private final InitialAmountGeneration amountGeneration = new InitialAmountGeneration(QUANTITY_GENERATION, 3, 0);
    private StudentsDataFiller dataFiller;

    @BeforeEach
    void before(){
        doReturn(expectedGroup).when(groupsDAO).findAll();
        dataFiller = new StudentsDataFiller(studentsGeneratorService, studentsDAO, groupsDAO);
    }

    @Test
    void testFillData_ShouldInsertBatchOfStudentsInDB(){
        when(studentsGeneratorService.getNameList()).thenReturn(names);
        when(studentsGeneratorService.getSurnameList()).thenReturn(surnames);
        when(studentsGeneratorService.getInitialAmountGeneration()).thenReturn(amountGeneration);

        dataFiller.fillData();

        verify(studentsDAO, times(1)).insertBatch(anyList());
        verify(groupsDAO, times(1)).findAll();
    }
}