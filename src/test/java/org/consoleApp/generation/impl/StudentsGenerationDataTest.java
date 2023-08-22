package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = StudentsGenerationData.class)
@TestPropertySource(properties = {"initialQuantityGenerations=3", "maxGroupSize=1", "minGroupSize=1"})
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

    @MockBean
    private StudentsGeneratorService studentsGeneratorService;

    @Autowired
    private StudentsGenerationData studentsGenerationData;


    @Test
    void testGenerateData_ShouldGenerateCorrectStudents(){
        when(studentsGeneratorService.getNameList()).thenReturn(names);
        when(studentsGeneratorService.getSurnameList()).thenReturn(surnames);

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