package org.consoleApp.parser;

import java.util.List;

public interface Parser<T> {
    T parse(String input);
    default List<T> parsedList(List<String> input){
        return input.stream().map(this::parse).toList();
    }
}
