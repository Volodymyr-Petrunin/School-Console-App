package org.consoleApp.fillingData;

import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.generationData.GroupGenerationData;
import org.consoleApp.groups.Group;
import org.consoleApp.groups.GroupsDAOImpl;
import org.consoleApp.readers.ResourcesFileReader;

import java.util.List;

public class GroupDataFiller implements DataFiller {
    private int quantityGenerations;
    private final DBConnector dbConnector = new DBConnector();
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dbConnector);
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
            groupsDAO.insert(new Group(currentIndex, result.get(currentIndex)));
        }
    }
}
