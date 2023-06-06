package org.consoleApp.generationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupGenerationData implements GenerationData{
    private final Random random = new Random();
    @Override
    public List<String> generationData(List<String> dataList, int quantity) {
        StringBuilder resultBuilder = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";

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
