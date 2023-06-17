package org.consoleApp.menu.leaf;

import org.consoleApp.menu.MenuItem;

import java.util.List;
import java.util.StringJoiner;

public class MenuOption implements MenuItem {
    private List<MenuItem> menuItems;
    private String dash;

    public MenuOption(List<MenuItem> menuItems, String dash) {
        this.menuItems = menuItems;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Please select an option: ";
    }

    @Override
    public void execute() {
        StringJoiner menu = new StringJoiner(System.lineSeparator());

        menu.add(getDescription());

        int place = 1;
        for (MenuItem item : menuItems){
            menu.add(place++ + ". " + item.getDescription());
        }

        menu.add("");

        menu.add("0. Exit");
        menu.add(dash);

        System.out.println(menu);
    }

}
