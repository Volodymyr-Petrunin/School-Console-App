package org.consoleApp.menu.composite;

import org.consoleApp.menu.MenuItem;

import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class MenuComposite implements MenuItem {
    private final Scanner scan = new Scanner(System.in);
    private List<MenuItem> menuItems;
    private String dash;
    private boolean exit;

    public MenuComposite(List<MenuItem> menuItems, String dash) {
        this.menuItems = menuItems;
        this.dash = dash;
    }

    @Override
    public String getDescription() {
        return "Please select an option: ";
    }

    @Override
    public void execute() {
        while (!exit){
            displayMenu();
            userChooses();
        }
    }

    private void displayMenu() {
        StringJoiner menu = new StringJoiner(System.lineSeparator());

        menu.add(getDescription());

        int place = 1;
        for (MenuItem item : menuItems) {
            menu.add(place++ + ". " + item.getDescription());
        }

        menu.add("");
        menu.add("0. Exit");
        menu.add(dash);

        System.out.println(menu);
    }

    private void userChooses(){
        System.out.print("Yor choice: ");
        int currentChoice = scan.nextInt();

        if (currentChoice >= 1 && currentChoice <= menuItems.size()) {
            menuItems.get(currentChoice - 1).execute();
        } else if (currentChoice == 0) {
            exit = true;
        }
    }
}
