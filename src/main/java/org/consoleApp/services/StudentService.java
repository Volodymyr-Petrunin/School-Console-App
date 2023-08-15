package org.consoleApp.services;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.dataFilling.DataFiller;
import org.consoleApp.domin.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements Services{
    private final StudentsDAO studentsDAO;
    private final DataFiller studentsDataFiller;
    private final DataFiller enrollDataFiller;

    @Autowired
    public StudentService(StudentsDAO studentsDAO, @Qualifier("studentsDataFiller") DataFiller studentsDataFiller, @Qualifier("enrollmentsDataFiller")DataFiller enrollDataFiller) {
        this.studentsDAO = studentsDAO;
        this.studentsDataFiller = studentsDataFiller;
        this.enrollDataFiller = enrollDataFiller;
    }

    public List<Student> getAllStudents() {
        return studentsDAO.findAll();
    }

    public Optional<Student> getStudentById(int studentId) {
        return studentsDAO.findById(studentId);
    }

    public boolean createStudent(Student student) {
        return studentsDAO.insert(student);
    }

    public void createMultipleStudents(List<Student> students) {
        studentsDAO.insertBatch(students);
    }

    public boolean updateStudent(Student student) {
        return studentsDAO.update(student);
    }

    public void deleteStudent(int studentId) {
        studentsDAO.findById(studentId).ifPresent(studentsDAO::delete);
    }

    public List<Student> getStudentsByFirstName(String firstName) {
        return studentsDAO.findByFirstName(firstName);
    }

    public boolean enrollStudentInCourse(int studentId, int courseId) {
        return studentsDAO.enrollStudentInCourse(studentId, courseId);
    }

    public boolean removeStudentFromCourse(int studentId, int courseId) {
        return studentsDAO.removeStudentFromCourse(studentId, courseId);
    }

    public List<Student> getStudentsByCourseName(String courseName) {
        return studentsDAO.findStudentsByCourseName(courseName);
    }

    @Override
    public void generateDataAndPopulateDB(){
        studentsDataFiller.fillData();
        enrollDataFiller.fillData();
    }
}
