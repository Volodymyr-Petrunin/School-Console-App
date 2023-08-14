package org.consoleApp.parametrizedDAOTest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@ComponentScan("org.consoleApp.dao")
@Import({DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
public class DaoTestConfig {
}
