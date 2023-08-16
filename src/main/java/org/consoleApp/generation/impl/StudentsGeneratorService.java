package org.consoleApp.generation.impl;

import org.consoleApp.generation.records.InitialAmountGeneration;
import org.consoleApp.readers.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsGeneratorService {
    private final Reader readerNames;
    private final Reader readerSurnames;
    private final InitialAmountGeneration initialAmountGeneration;

    @Autowired
    public StudentsGeneratorService(@Qualifier("readNameFile") Reader readerNames, @Qualifier("readSurnameFile") Reader readerSurnames, InitialAmountGeneration initialAmountGeneration) {
        this.readerNames = readerNames;
        this.readerSurnames = readerSurnames;
        this.initialAmountGeneration = initialAmountGeneration;
    }

    public List<String> getNameList(){
        return readerNames.read();
    }

    public List<String> getSurnameList(){
        return readerSurnames.read();
    }

    public InitialAmountGeneration getInitialAmountGeneration(){
        return initialAmountGeneration;
    }
}
