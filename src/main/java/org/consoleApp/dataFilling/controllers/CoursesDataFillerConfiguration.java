package org.consoleApp.dataFilling.controllers;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.dataFilling.leaf.CoursesDataFiller;
import org.consoleApp.generation.impl.CoursesGeneratorService;
import org.consoleApp.readers.ResourcesFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:coursesDataFillerConfiguration.properties")
public class CoursesDataFillerConfiguration {
    @Bean
    public ResourcesFileReader readCoursesFile(@Value("${courses}") String filename){
        return new ResourcesFileReader(filename);
    }

    @Bean
    public DataFiller courseDataFiller(CoursesGeneratorService courses, CourseDAO courseDAO) {
        return new CoursesDataFiller(courses, courseDAO);
    }
}
