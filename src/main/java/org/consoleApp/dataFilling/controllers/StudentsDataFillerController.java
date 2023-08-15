package org.consoleApp.dataFilling.controllers;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.leaf.StudentsDataFiller;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.consoleApp.readers.Reader;
import org.consoleApp.readers.ResourcesFileReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Controller
@PropertySource("classpath:studentsDataFillerController.properties")
public class StudentsDataFillerController {
    @Bean
    public ResourcesFileReader readNameFile(@Value("${name}") String filename){
        return new ResourcesFileReader(filename);
    }

    @Bean
    public ResourcesFileReader readSurnameFile(@Value("${surname}") String filename){
        return new ResourcesFileReader(filename);
    }

    @Bean
    public InitialAmountGeneration initialAmountGeneration(@Value("${initialQuantityGenerations}")int initialQuantityGenerations,
                                                           @Value("${maxGroupSize}") int maxGroupSize,
                                                           @Value("${minGroupSize}") int minGroupSize){
        return new InitialAmountGeneration(initialQuantityGenerations, maxGroupSize, minGroupSize);
    }

    @Bean
    public StudentsDataFiller studentsDataFiller(@Qualifier("readNameFile") Reader readerNames,
                                                 @Qualifier("readSurnameFile") Reader readerSurname,
                                                 @Qualifier("initialAmountGeneration") InitialAmountGeneration initialAmountGeneration,
                                                 StudentsDAO studentsDAO, GroupDAO groupDAO){
        return new StudentsDataFiller(readerNames.read(), readerSurname.read(), initialAmountGeneration, studentsDAO, groupDAO);
    }
}
