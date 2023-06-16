package org.consoleApp.generation.impl;

import org.consoleApp.generation.records.GroupAmountGeneration;
import org.consoleApp.domin.Group;
import org.consoleApp.generation.GenerationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupGenerationData implements GenerationData<Group> {
    private final Random random = new Random();
    private static final int ALPHABET_SIZE = 26;
    private static final int DIGITS_SIZE = 10;
    private int quantity;
    private int amountOfLetters;
    private int amountOfNumbers;

    public GroupGenerationData(GroupAmountGeneration groupAmountGeneration) {
        this.quantity = groupAmountGeneration.quantityGenerations();
        this.amountOfLetters = groupAmountGeneration.amountOfLetters();
        this.amountOfNumbers = groupAmountGeneration.amountOfNumbers();
    }

    @Override
    public List<Group> generateData() {
        StringBuilder resultBuilder = new StringBuilder();

        List<Group> result = new ArrayList<>();

        for (int index = 0; index < quantity; index++){
            resultBuilder.append(generateRandomChars(amountOfLetters, true));
            resultBuilder.append("-");
            resultBuilder.append(generateRandomChars(amountOfNumbers, false));

            result.add(new Group(null, resultBuilder.toString()));
            resultBuilder.setLength(0);
        }
        return result;
    }

    private String generateRandomChars(int count, boolean isLetter) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int index = 0; index < count; index++) {
            char currentChar;
            if (isLetter) {
                currentChar = (char) (random.nextInt(ALPHABET_SIZE) + 'A');
            } else {
                currentChar = (char) (random.nextInt(DIGITS_SIZE) + '0');
            }
            stringBuilder.append(currentChar);
        }

        return stringBuilder.toString();
    }
}
