package org.consoleApp.menu.leaf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class SystemUtils {
    private static InputStream originalIn;
    private static PrintStream originalOut;
    private static InputStream input;
    private static ByteArrayOutputStream output;

    public static void setSystemInput(String inputString) {
        originalIn = System.in;
        input = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(input);
    }

    public static void setSystemOutput() {
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
    }

    public static String getSystemOutput() {
        return output.toString();
    }

    public static void restoreSystemInputAndOutput() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
}
