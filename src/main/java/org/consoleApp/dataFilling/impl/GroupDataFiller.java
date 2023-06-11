package org.consoleApp.dataFilling.impl;

import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.GroupGenerationData;
import org.consoleApp.domin.Group;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;

import javax.sql.DataSource;
import java.util.List;

public class GroupDataFiller implements DataFiller {
    private int quantityGenerations;
    private int amountOfLetters;
    private int amountOfNumbers;
    private GroupsDAOImpl groupsDAO;
    private GroupGenerationData generationData;
    public GroupDataFiller(int quantityGenerations,int  amountOfLetters, int amountOfNumbers,DataSource dataSource) {
        this.quantityGenerations = quantityGenerations;
        this.amountOfLetters = amountOfLetters;
        this.amountOfNumbers = amountOfNumbers;
        this.groupsDAO = new GroupsDAOImpl(dataSource);
        this.generationData = new GroupGenerationData(quantityGenerations,amountOfLetters,amountOfNumbers, dataSource);
    }

    @Override
    public void fillData() {
        List<Group> groups = generationData.generateData();
        groupsDAO.insertBatch(groups);
    }
}
