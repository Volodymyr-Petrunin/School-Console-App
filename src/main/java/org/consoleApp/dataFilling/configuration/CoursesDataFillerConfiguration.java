package org.consoleApp.dataFilling.configuration;


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
}
