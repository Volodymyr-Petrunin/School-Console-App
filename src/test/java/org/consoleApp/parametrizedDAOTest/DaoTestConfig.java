package org.consoleApp.parametrizedDAOTest;

import org.consoleApp.dao.CourseDAO;
import org.consoleApp.dao.GroupDAO;
import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dao.jdbc.CourseDAOImpl;
import org.consoleApp.dao.jdbc.GroupsDAOImpl;
import org.consoleApp.dao.jdbc.StudentsDAOImpl;
import org.consoleApp.dao.spring_jdbc.CourseRepository;
import org.consoleApp.dao.spring_jdbc.GroupRepository;
import org.consoleApp.dao.spring_jdbc.StudentsRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootConfiguration
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

    @Bean
    public StudentsDAO studentsImpl(DataSource dataSource){
        return new StudentsDAOImpl(dataSource);
    }

    @Bean
    public StudentsDAO studentsRepo(JdbcTemplate jdbcTemplate){
        return new StudentsRepository(jdbcTemplate);
    }
}
