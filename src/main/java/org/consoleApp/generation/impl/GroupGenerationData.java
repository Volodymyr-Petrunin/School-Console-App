package org.consoleApp.generation.impl;

import org.consoleApp.generation.GenerationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupGenerationData implements GenerationData {
    private final Random random = new Random();
    private List<String> caractersList;
    private List<String> numbersList;
    private int quantity;
    private int amountOfLetters;
    private int amountOfNumbers;

    public GroupGenerationData(List<String> caractersList, List<String> numbersList, int quantity, int amountOfLetters, int amountOfNumbers) {
        this.caractersList = caractersList;
        this.numbersList = numbersList;
        this.quantity = quantity;
        this.amountOfLetters = amountOfLetters;
        this.amountOfNumbers = amountOfNumbers;
    }

    @Override
    public List<String> generationData() {
        StringBuilder resultBuilder = new StringBuilder();

        List<String> result = new ArrayList<>();

        for (int index = 0; index < quantity; index++){
            resultBuilder.append(randomChar(caractersList.get(0),amountOfLetters)); // get 0 because in file characters 1 line
            resultBuilder.append("-");
            resultBuilder.append(randomChar(numbersList.get(0),amountOfNumbers)); // same here but file numbers

            result.add(resultBuilder.toString());
            resultBuilder.setLength(0);
        }
        return result;
    }

    private String randomChar(String characters, int quantity){
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < quantity; i++){
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            result.append(randomChar);
        }
        return result.toString();
    }
}
