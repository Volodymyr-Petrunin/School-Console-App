package org.consoleApp.generation.impl;

import org.consoleApp.domin.Group;
import org.consoleApp.readers.Reader;
import org.consoleApp.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsGeneratorService {
    private final Reader readerNames;
    private final Reader readerSurnames;
    private final GroupService groupService;

    @Autowired
    public StudentsGeneratorService(@Qualifier("readNameFile") Reader readerNames, @Qualifier("readSurnameFile") Reader readerSurnames, GroupService groupService) {
        this.readerNames = readerNames;
        this.readerSurnames = readerSurnames;
        this.groupService = groupService;
    }

    public List<String> getNameList(){
        return readerNames.read();
    }

    public List<String> getSurnameList(){
        return readerSurnames.read();
    }

    public List<Group> getGroupList(){
        return groupService.getAllGroups();
    }
}
