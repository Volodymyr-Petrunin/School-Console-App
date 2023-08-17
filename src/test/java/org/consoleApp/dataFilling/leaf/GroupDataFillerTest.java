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
    @Mock private GroupsDAOImpl groupsDAO;
    private GroupDataFiller dataFiller;

    @BeforeEach
    void setup(){
        dataFiller = new GroupDataFiller(groupsDAO);
    }

    @Test
    void testFillData_ShouldUseCorrectLogic(){
        dataFiller.fillData();
        verify(groupsDAO, times(1)).insertBatch(anyList());
    }
}