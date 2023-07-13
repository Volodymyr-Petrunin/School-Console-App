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
        this.amountOfNumbers = groupAmountGeneration.amountOfDigits();
    }

    @Override
    public List<Group> generateData() {
        List<Group> result = new ArrayList<>();

        for (int index = 0; index < quantity; index++){
            String name = generateRandomChars(amountOfLetters, true) + "-" + generateRandomChars(amountOfNumbers, false);

            result.add(new Group(null, name));
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
