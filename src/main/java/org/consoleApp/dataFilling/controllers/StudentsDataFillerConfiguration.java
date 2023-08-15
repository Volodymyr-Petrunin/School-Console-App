package org.consoleApp.dataFilling.controllers;

import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.leaf.StudentsDataFiller;
import org.consoleApp.generation.impl.StudentsReaderService;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.consoleApp.readers.Reader;
import org.consoleApp.readers.ResourcesFileReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:studentsDataFillerConfiguration.properties")
public class StudentsDataFillerConfiguration {
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
    public StudentsDataFiller studentsDataFiller(StudentsReaderService studentsReaderService, StudentsDAO studentsDAO, GroupDAO groupDAO){
        return new StudentsDataFiller(studentsReaderService, studentsDAO, groupDAO);
    }
}
