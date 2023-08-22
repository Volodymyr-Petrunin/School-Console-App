package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GroupGenerationData.class)
@TestPropertySource(properties= {"quantityGenerations=3", "amountOfLetters=2", "amountOfDigits=2"})
class GroupGenerationDataTest {
    @Autowired private GroupGenerationData groupGenerationData;

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