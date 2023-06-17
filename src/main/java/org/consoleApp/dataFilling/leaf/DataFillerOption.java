package org.consoleApp.dataFilling.leaf;

import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.dataFilling.impl.CoursesDataFiller;
import org.consoleApp.dataFilling.impl.EnrollmentsDataFiller;
import org.consoleApp.dataFilling.impl.GroupDataFiller;
import org.consoleApp.dataFilling.impl.StudentsDataFiller;

public class DataFillerOption implements DataFiller {
    private CoursesDataFiller coursesDataFiller;
    private GroupDataFiller groupDataFiller;
    private StudentsDataFiller studentsDataFiller;
    private EnrollmentsDataFiller enrollmentsDataFiller;

    public DataFillerOption(CoursesDataFiller coursesDataFiller, GroupDataFiller groupDataFiller, StudentsDataFiller studentsDataFiller, EnrollmentsDataFiller enrollmentsDataFiller) {
        this.coursesDataFiller = coursesDataFiller;
        this.groupDataFiller = groupDataFiller;
        this.studentsDataFiller = studentsDataFiller;
        this.enrollmentsDataFiller = enrollmentsDataFiller;
    }

    @Override
    public void fillData() {
        coursesDataFiller.fillData();
        groupDataFiller.fillData();
        studentsDataFiller.fillData();
        enrollmentsDataFiller.fillData();
    }
}
