package org.consoleApp.dao.spring_jpa;

import org.consoleApp.domin.Group;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("spring-data-jpa")
public interface GroupRepositoryJPA extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g WHERE (SELECT COUNT(*) FROM Student s WHERE s.group.id = g.id) <= :maxStudents")
    List<Group> findGroupsWithLessOrEqualStudents(@Param("maxStudents") int maxStudents);
}
