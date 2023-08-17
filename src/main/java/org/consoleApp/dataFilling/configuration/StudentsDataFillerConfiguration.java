package org.consoleApp.dataFilling.configuration;

import org.consoleApp.readers.ResourcesFileReader;
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
}
