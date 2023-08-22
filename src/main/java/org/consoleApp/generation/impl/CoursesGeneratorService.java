package org.consoleApp.generation.impl;

import org.consoleApp.domin.Course;
import org.consoleApp.generation.GenerationData;
import org.consoleApp.parser.Parser;
import org.consoleApp.readers.ResourcesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesGeneratorService implements GenerationData<Course> {
    private final Parser<Course> courseParser;
    private final ResourcesFileReader resourcesFileReader;

    @Autowired
    public CoursesGeneratorService(Parser<Course> courseParser, @Qualifier("readCoursesFile") ResourcesFileReader resourcesFileReader) {
        this.courseParser = courseParser;
        this.resourcesFileReader = resourcesFileReader;
    }

    @Override
    public List<Course> generateData() {
        return courseParser.parsedList(resourcesFileReader.read());
    }
}
