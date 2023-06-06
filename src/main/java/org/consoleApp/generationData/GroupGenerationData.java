package org.consoleApp.generationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupGenerationData implements GenerationData{
    private final Random random = new Random();
    private List<String> nameList;
    private List<String> numberList;
    private int quantity;

    public GroupGenerationData(List<String> dataNameList, List<String> dataNumberList, int quantity) {
        this.nameList = dataNameList;
        this.numberList = dataNumberList;
        this.quantity = quantity;
    }
    @Override
    public List<String> generationData() {
        StringBuilder resultBuilder = new StringBuilder();

        List<String> result = new ArrayList<>();

        for (int index = 0; index < quantity; index++){
            resultBuilder.append(randomChar(nameList.get(0),2)); // get 0 because in file 1 line
            resultBuilder.append("-");
            resultBuilder.append(randomChar(numberList.get(0),2)); // same here

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
