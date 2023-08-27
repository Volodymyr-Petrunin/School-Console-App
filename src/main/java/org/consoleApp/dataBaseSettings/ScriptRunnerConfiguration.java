package org.consoleApp.dataBaseSettings;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.io.InputStream;

@Configuration
@PropertySource("classpath:ScriptRunnerConfiguration.properties")
public class ScriptRunnerConfiguration {

    @Bean
    public InputStream createTableStream(@Value("${createTables}") String createTables){
        return getClass().getResourceAsStream(createTables);
    }

    @Bean
    public ScriptRunner scriptRunner(DataSource dataSource, @Qualifier("createTableStream") InputStream inputStream){
        return new ScriptRunner(dataSource, inputStream);
    }
}
