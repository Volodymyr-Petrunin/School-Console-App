package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupGenerationDataTest {
    private final GroupAmountGeneration groupAmountGeneration = new GroupAmountGeneration(3, 2, 2);
    private final GroupGenerationData groupGenerationData = new GroupGenerationData(groupAmountGeneration);

    @Test
    void testGenerateData_ShouldReturnCorrectListOfGroup(){
        List<Group> actual = groupGenerationData.generateData();

        int listSize = 3;
        assertEquals(listSize, actual.size());
        assertNotNull(actual);

        int expectedLetters = 2;
        int expectedNumbers = 2;

        for (Group actualGroup : actual) {
            assertNotNull(actualGroup);
            assertNotNull(actualGroup.getName());

            String[] nameParts = actualGroup.getName().split("-");
            assertEquals(expectedLetters, nameParts[0].length());
            assertEquals(expectedNumbers, nameParts[1].length());
        }
    }
}