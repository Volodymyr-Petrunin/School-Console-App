package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.domin.Group;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupDataFillerTest extends AbstractContainerBaseTest {
    private final static int QUANTITY_GENERATION = 3;
    private final static int AMOUNT_OF_LETTERS = 2;
    private final static int AMOUNT_OF_DIGITS = 2;
    private final DataSource dataSource = getDataSource();
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final GroupAmountGeneration amountGeneration = new GroupAmountGeneration(QUANTITY_GENERATION, AMOUNT_OF_LETTERS, AMOUNT_OF_DIGITS);
    private final GroupDataFiller dataFiller = new GroupDataFiller(amountGeneration, groupsDAO);

    @Test
    void testFillData_ShouldInsertBatchOfGroupsInDB(){
        dataFiller.fillData();

        List<Group> actual = groupsDAO.findAll();
        assertNotNull(actual);
        assertEquals(QUANTITY_GENERATION, actual.size());

        for (Group group : actual){
            assertNotEquals(0, group.getId());
            assertNotNull(group.getName());

            String[] nameParts = group.getName().split("-");
            assertEquals(AMOUNT_OF_LETTERS, nameParts[0].length());
            assertEquals(AMOUNT_OF_DIGITS, nameParts[1].length());
        }
    }
}