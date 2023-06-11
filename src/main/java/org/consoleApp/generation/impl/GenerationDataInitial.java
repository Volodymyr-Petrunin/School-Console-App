package org.consoleApp.generation.impl;

import org.consoleApp.generation.GenerationData;

import java.util.*;

public class GenerationDataInitial implements GenerationData {
    private final Random random = new Random();
    private List<String> dataList;
    private int quantity;

    public GenerationDataInitial(List<String> dataList, int quantity) {
        this.dataList = dataList;
        this.quantity = quantity;
    }

    @Override
    public List<String> generationData(){
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
}
