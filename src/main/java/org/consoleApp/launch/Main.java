package org.consoleApp.launch;


import org.consoleApp.launch.LaunchApp;

public class Main {
    public static void main(String[] args) {
        LaunchApp launchApp = new LaunchApp();
        launchApp.fillData();
        launchApp.launch();
    }
}