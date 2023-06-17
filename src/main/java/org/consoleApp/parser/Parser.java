package org.consoleApp.parser;

import java.util.List;

public interface Parser<T> {
    T parse(String input);
    List<T> parsedList(List<String> list);
}
