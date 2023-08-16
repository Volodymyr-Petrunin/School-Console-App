package org.consoleApp.dataFilling.configuration;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.generation.records.EnrollInfo;
import org.consoleApp.generation.impl.EnrollmentsDataGeneration;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;
import org.consoleApp.generation.impl.StudentsGenerationData;
import org.consoleApp.generation.impl.StudentsGeneratorService;
import org.consoleApp.generation.records.InitialAmountGeneration;
import org.consoleApp.readers.ResourcesFileReader;
import org.consoleApp.services.GroupService;
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
    public GenerationData<EnrollInfo> enrollmentsDataFiller(@Value("${numberOfStudentInOneCourse}") int numberOfStudentInOneCourse,
                                                            StudentsDAO studentsDAO, CourseDAO courseDAO){
        return new EnrollmentsDataGeneration(numberOfStudentInOneCourse, studentsDAO, courseDAO);
    }

    @Bean
    public GenerationData<Student> studentsGeneration(StudentsGeneratorService studentsGeneratorService, GroupService groupService){
        return new StudentsGenerationData(studentsGeneratorService, groupService.getAllGroups());
    }
}
