package org.consoleApp.generationData;

import java.util.*;

public class GenerationTestData {
    private final Random random = new Random();

    public List<String> generationData(List<String> dataList,int quantity){
        List<String> resultData = new ArrayList<>();

        for (int currentIndex = 0; currentIndex < quantity; currentIndex++){
            String currentData = getRandomElement(dataList);
            resultData.add(currentData);
        }
        return resultData;
    }

    private <T> T getRandomElement(List<T> list){
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public List<String> generationGroups(int quantity){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        StringBuilder resultBuilder = new StringBuilder();

        List<String> result = new ArrayList<>();

        for (int i = 0; i < quantity; i++){
            resultBuilder.append(randomChar(characters,2));
            resultBuilder.append("-");
            resultBuilder.append(randomChar(numbers,2));

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
