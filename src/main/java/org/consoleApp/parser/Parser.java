package org.consoleApp.parser;

public interface Parser<T> {
    T parse(String input);
}
