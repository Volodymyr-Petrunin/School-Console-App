package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    private final StudentsGenerationData studentsGenerationData = new StudentsGenerationData(names, surnames, amountGeneration, groups);


    @Test
    void testGenerateData_ShouldGenerateCorrectStudents(){
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