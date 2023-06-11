package org.consoleApp.dataFilling.impl;

import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.GroupGenerationData;
import org.consoleApp.domin.Group;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.readers.ResourcesFileReader;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

public class GroupDataFiller implements DataFiller {
    private final Random random = new Random();
    private int quantityGenerations;
    private GroupsDAOImpl groupsDAO;
    private final ResourcesFileReader readCharacters = new ResourcesFileReader("characters.txt");
    private final ResourcesFileReader readNumbers = new ResourcesFileReader("numbers.txt");
    public GroupDataFiller(int quantityGenerations, DataSource dataSource) {
        this.quantityGenerations = quantityGenerations;
        this.groupsDAO = new GroupsDAOImpl(dataSource);
    }

    @Override
    public void fillData() {
        List<String> characters = readCharacters.read();
        List<String> numbers = readNumbers.read();

        GroupGenerationData generationData = new GroupGenerationData(characters,numbers,quantityGenerations,2,2);
        List<String> result = generationData.generationData();

        for (int currentIndex = 0; currentIndex < quantityGenerations; currentIndex++){
            int nextGroupId = groupsDAO.getNextId();
            groupsDAO.insert(new Group(nextGroupId, result.get(currentIndex)));
        }
    }
}
