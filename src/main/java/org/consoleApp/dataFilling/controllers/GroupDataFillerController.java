package org.consoleApp.dataFilling.controllers;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.dataFilling.leaf.GroupDataFiller;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Controller
@PropertySource("classpath:groupDataFillerController.properties")
public class GroupDataFillerController {
    @Bean
    public GroupAmountGeneration groupAmountGeneration(@Value("${quantityGenerations}") int quantityGenerations,
                                                       @Value("${amountOfLetters}") int amountOfLetters,
                                                       @Value("${amountOfDigits}") int amountOfDigits){
        return new GroupAmountGeneration(quantityGenerations, amountOfLetters, amountOfDigits);
    }

    @Bean
    public DataFiller groupDataFiller(@Qualifier("groupAmountGeneration") GroupAmountGeneration groupAmountGeneration, GroupDAO groupDAO){
        return new GroupDataFiller(groupAmountGeneration, groupDAO);
    }
}
