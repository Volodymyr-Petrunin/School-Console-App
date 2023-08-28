package org.consoleApp.menu.helper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuConfiguration {

    @Bean
    public String dash(){
        return "-".repeat(50);
    }
}
