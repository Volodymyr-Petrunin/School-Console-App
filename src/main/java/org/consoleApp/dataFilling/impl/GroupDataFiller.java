package org.consoleApp.dataFilling.impl;

import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.generation.impl.GroupGenerationData;
import org.consoleApp.domin.Group;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.readers.ResourcesFileReader;

import javax.sql.DataSource;
import java.util.List;

public class GroupDataFiller implements DataFiller {
    private int quantityGenerations;
    private final DBConnector dbConnector = new DBConnector(10);
    private final DataSource dataSource = dbConnector.getConnection();
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dataSource);
    private final ResourcesFileReader readCharacters = new ResourcesFileReader("characters.txt");
    private final ResourcesFileReader readNumbers = new ResourcesFileReader("numbers.txt");
    public GroupDataFiller(int quantityGenerations) {
        this.quantityGenerations = quantityGenerations;
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
