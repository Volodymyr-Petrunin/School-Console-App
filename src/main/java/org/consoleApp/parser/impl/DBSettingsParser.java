package org.consoleApp.parser.impl;

import org.consoleApp.parser.Parser;
import org.consoleApp.records.DBSettings;

public class DBSettingsParser implements Parser<DBSettings> {
    @Override
    public DBSettings parse(String input) {
        String[] settings = input.split("_");

        if (settings.length != 5) {
            throw new IllegalArgumentException("Invalid input string: " + input);
        }

        String host = settings[0];
        String port = settings[1];
        String dbName = settings[2];
        String login = settings[3];
        String password = settings[4];

        return new DBSettings(host, port, dbName, login, password);
    }
}
