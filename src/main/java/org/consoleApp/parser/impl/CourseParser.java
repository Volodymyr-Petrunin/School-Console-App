package org.consoleApp.parser.impl;

import org.consoleApp.domin.Course;
import org.consoleApp.parser.Parser;


public class CourseParser implements Parser<Course> {
    @Override
    public Course parse(String input) {
       if (!input.contains("_")){
           throw new IllegalArgumentException("Not correct string" + input);
       }

       String[] info = input.split("_",2);
       String name = info[0];
       String description = info[1];

       return new Course(null, name, description);
    }
}
