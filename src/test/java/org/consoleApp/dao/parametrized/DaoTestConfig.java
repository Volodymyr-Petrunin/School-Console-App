package org.consoleApp.dao.parametrized;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.jdbc.AbstractContainerBaseTest;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.spring_jdbc.CourseRepository;
import org.consoleApp.dao.spring_jdbc.GroupRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

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
    public JdbcTemplate testJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CourseDAO courseImpl(DataSource dataSource){
        return new CourseDAOImpl(dataSource);
    }

    @Bean
    public CourseDAO courseRepo(JdbcTemplate jdbcTemplate){
        return new CourseRepository(jdbcTemplate);
    }

    @Bean
    public GroupDAO groupImpl(DataSource dataSource){
        return new GroupsDAOImpl(dataSource);
    }

    @Bean
    public GroupDAO groupRepo(JdbcTemplate jdbcTemplate){
        return new GroupRepository(jdbcTemplate);
    }
}
