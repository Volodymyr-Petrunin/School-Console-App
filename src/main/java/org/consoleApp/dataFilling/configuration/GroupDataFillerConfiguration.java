package org.consoleApp.dataFilling.configuration;

import org.consoleApp.domin.Group;
import org.consoleApp.generation.GenerationData;
import org.consoleApp.generation.impl.GroupGenerationData;
import org.consoleApp.generation.records.GroupAmountGeneration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:groupDataFillerConfiguration.properties")
public class GroupDataFillerConfiguration {
    @Bean
    public GroupAmountGeneration groupAmountGeneration(@Value("${quantityGenerations}") int quantityGenerations,
                                                       @Value("${amountOfLetters}") int amountOfLetters,
                                                       @Value("${amountOfDigits}") int amountOfDigits){
        return new GroupAmountGeneration(quantityGenerations, amountOfLetters, amountOfDigits);
    }

    @Bean
    public GenerationData<Group> groupGenerationData(@Qualifier("groupAmountGeneration") GroupAmountGeneration groupAmountGeneration){
        return new GroupGenerationData(groupAmountGeneration);
    }
}
