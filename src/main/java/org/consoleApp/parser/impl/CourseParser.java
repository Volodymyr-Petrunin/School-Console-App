package org.consoleApp.parser.impl;

import org.consoleApp.parser.Parser;
import org.consoleApp.records.CourseInfo;

public class CourseParser implements Parser<CourseInfo> {
    @Override
    public CourseInfo parse(String input) {
       if (!input.contains("_")){
           throw new IllegalArgumentException("Not correct string" + input);
       }

       String[] info = input.split("_",2);
       String name = info[0];
       String description = info[1];

       return new CourseInfo(name, description);
    }
}
