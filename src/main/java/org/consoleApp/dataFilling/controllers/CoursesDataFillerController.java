package org.consoleApp.dataFilling.controllers;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.dataFilling.leaf.CoursesDataFiller;
import org.consoleApp.domin.Course;
import org.consoleApp.parser.Parser;
import org.consoleApp.parser.impl.CourseParser;
import org.consoleApp.readers.Reader;
import org.consoleApp.readers.ResourcesFileReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Controller
@PropertySource("classpath:coursesDataFillerController.properties")
public class CoursesDataFillerController {
    @Bean
    public ResourcesFileReader readCoursesFile(@Value("${courses}") String filename){
        return new ResourcesFileReader(filename);
    }
    @Bean
    public Parser<Course> courseParser(){
        return new CourseParser();
    }

    @Bean
    public DataFiller courseDataFiller(Parser<Course> parser, @Qualifier("readCoursesFile") Reader reader, CourseDAO courseDAO) {
        return new CoursesDataFiller(parser.parsedList(reader.read()), courseDAO);
    }
}
