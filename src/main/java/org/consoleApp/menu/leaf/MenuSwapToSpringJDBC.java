package org.consoleApp.menu.leaf;

import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.composite.MenuComposite;

import java.util.List;
import java.util.StringJoiner;

public class MenuSwapToSpringJDBC extends MenuComposite implements MenuItem{
    private final String swap = "Swap to JDBC";

    public MenuSwapToSpringJDBC(List<MenuItem> menuItems, String dash) {
        super(menuItems, dash);
    }

    @Override
    public String getDescription() {
        return new StringJoiner(System.lineSeparator())
                .add("You use Spring JDBC")
                .add("Please select an option: ").toString();
    }

    @Override
    public void execute() {
        boolean exit = false;

        while (!exit){
            super.displayMenu(swap);
            exit = super.userChooses(getSuper());
        }
    }
}
