package org.consoleApp.menu.composite;

import org.consoleApp.menu.MenuItem;

import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class MenuComposite implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private final String swap = "Swap to Spring JDBC";
    private List<MenuItem> menuItems;
    private String dash;
    private MenuItem swapItem;
    private boolean exit;

    public MenuComposite(List<MenuItem> menuItems, MenuItem swapItem, String dash) {
        this.menuItems = menuItems;
        this.dash = dash;
        this.swapItem = swapItem;
    }

    public MenuComposite(List<MenuItem> menuItems, String dash) {
        this.menuItems = menuItems;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return new StringJoiner(System.lineSeparator())
                .add("You use JDBC")
                .add("Please select an option: ").toString();
    }

    @Override
    public void execute() {
        while (!exit){
            displayMenu(swap);
            userChooses(swapItem);
        }
    }

    protected void displayMenu(String swap) {
        StringJoiner menu = new StringJoiner(System.lineSeparator());

        menu.add(getDescription());

        int place = 1;
        for (MenuItem item : menuItems) {
            menu.add(place++ + ". " + item.getDescription());
        }

        menu.add("");
        menu.add(place + ". " + swap);
        menu.add("0. Exit");
        menu.add(dash);

        System.out.println(menu);
    }

    protected boolean userChooses(MenuItem swapItem){
        System.out.print("Yor choice: ");
        int currentChoice = scan.nextInt();

        if (currentChoice >= 1 && currentChoice <= menuItems.size()) {
            menuItems.get(currentChoice - 1).execute();
        } else if (currentChoice == 0) {
            return exit = true;
        } else if (currentChoice == menuItems.size() + 1){
            swapItem.execute();
        }

        return false;
    }

    protected MenuItem getSuper(){
        return new MenuComposite(menuItems, this, dash);
    }
}
