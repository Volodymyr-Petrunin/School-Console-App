package org.consoleApp.fillingData;

import org.consoleApp.generationData.GenerationTestData;
import org.consoleApp.dataBaseSettings.DBConnector;
import org.consoleApp.groups.Group;
import org.consoleApp.groups.GroupsDAOImpl;

import java.util.List;

public class GroupDataFiller implements DataFiller {
    private int quantityGenerations;
    private final GenerationTestData generate = new GenerationTestData();
    private final DBConnector dbConnector = new DBConnector();
    private final GroupsDAOImpl groupsDAO = new GroupsDAOImpl(dbConnector);
    public GroupDataFiller(int quantityGenerations) {
        this.quantityGenerations = quantityGenerations;
    }

    @Override
    public void fillData() {
        List<String> groupName = generate.generationGroups(quantityGenerations);
        for (int currentIndex = 0; currentIndex < quantityGenerations; currentIndex++){
            groupsDAO.insert(new Group(currentIndex,groupName.get(currentIndex)));
        }
    }
}
