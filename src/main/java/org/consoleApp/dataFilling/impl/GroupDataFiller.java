package org.consoleApp.dataFilling.impl;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.GroupGenerationData;
import org.consoleApp.domin.Group;

import java.util.List;

public class GroupDataFiller implements DataFiller {
    private GroupDAO groupsDAO;
    private GroupGenerationData generationData;
    public GroupDataFiller(int quantityGenerations,int  amountOfLetters, int amountOfNumbers, GroupDAO groupsDAO) {
        this.groupsDAO = groupsDAO;
        this.generationData = new GroupGenerationData(quantityGenerations,amountOfLetters,amountOfNumbers);
    }

    @Override
    public void fillData() {
        List<Group> groups = generationData.generateData();
        groupsDAO.insertBatch(groups);
    }
}
