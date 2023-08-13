package org.consoleApp.dao.parametrized;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.spring_jdbc.CourseRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@SpringBootConfiguration
@ComponentScan("org.consoleApp.dao")
@Profile("jdbc-test")
public class DaoTestConfig extends AbstractContainerBaseTest {
    @Bean
    public DataSource testDataSource(){
        return getDataSource();
    }

    @Bean
    public CourseDAO courseImpl(DataSource dataSource){
        return new CourseDAOImpl(dataSource);
    }

    @Bean
    public CourseDAO courseRepo(DataSource dataSource){
        return new CourseRepository(dataSource);
    }
}
