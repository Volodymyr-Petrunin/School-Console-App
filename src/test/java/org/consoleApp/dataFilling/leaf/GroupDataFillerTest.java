package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupDataFillerTest {
    private final static int QUANTITY_GENERATION = 3;
    private final static int AMOUNT_OF_LETTERS = 2;
    private final static int AMOUNT_OF_DIGITS = 2;
    private final GroupAmountGeneration amountGeneration = new GroupAmountGeneration(QUANTITY_GENERATION, AMOUNT_OF_LETTERS, AMOUNT_OF_DIGITS);
    @Mock private GroupsDAOImpl groupsDAO;
    private GroupDataFiller dataFiller;

    @BeforeEach
    void setup(){
        dataFiller = new GroupDataFiller(amountGeneration, groupsDAO);
    }

    @Test
    void testFillData_ShouldUseCorrectLogic(){
        dataFiller.fillData();
        verify(groupsDAO, times(1)).insertBatch(anyList());
    }
}