package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class StudentsGenerationDataTest {
    private final List<String> names = List.of(
            "Vova", "Stas", "Dima"
    );
    private final List<String> surnames = List.of(
            "Petrunin", "Solyanik", "Dypai"
    );
    private final List<Group> groups = List.of(
            new Group(1, "AA-11"),
            new Group(2, "BB-22"),
            new Group(3, "CC-33")
    );
    private final InitialAmountGeneration amountGeneration = new InitialAmountGeneration(3, 2, 1);
    @Mock private StudentsGeneratorService studentsGeneratorService;
    private final StudentsGenerationData studentsGenerationData = new StudentsGenerationData(studentsGeneratorService, groups);


    @Test
    void testGenerateData_ShouldGenerateCorrectStudents(){
        when(studentsGeneratorService.getNameList()).thenReturn(names);
        when(studentsGeneratorService.getSurnameList()).thenReturn(surnames);
        when(studentsGeneratorService.getInitialAmountGeneration()).thenReturn(amountGeneration);

        List<Student> actual = studentsGenerationData.generateData();

        assertEquals(3, actual.size());

        for (Student student : actual){
            assertNotNull(student.getGroupId());
            assertNotNull(student.getFirstName());
            assertNotNull(student.getLastName());

            assertTrue(names.contains(student.getFirstName()));
            assertTrue(surnames.contains(student.getLastName()));
        }
    }
}