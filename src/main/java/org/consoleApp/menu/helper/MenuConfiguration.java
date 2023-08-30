package org.consoleApp.menu.helper;

import org.consoleApp.menu.MenuItem;
import org.consoleApp.menu.composite.MenuComposite;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MenuConfiguration {
    @Bean
    public String dash(){
        return "-".repeat(50);
    }

    @Bean
    public MenuComposite menuComposite(List<MenuItem> menuItems, @Qualifier("dash") String dash){
        return new MenuComposite(menuItems, dash);
    }
}
