package org.consoleApp.services;

import org.consoleApp.dao.StudentsDAO;
import org.consoleApp.generation.records.EnrollInfo;
import org.consoleApp.domin.Student;
import org.consoleApp.generation.GenerationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements DataFiller {
    private final StudentsDAO studentsDAO;
    private final GenerationData<EnrollInfo> enrollInfoGenerationData;
    private final GenerationData<Student> studentGenerationData;

    @Autowired
    public StudentService(StudentsDAO studentsDAO, @Qualifier("enrollmentsDataGeneration")GenerationData<EnrollInfo> enrollInfoGenerationData,
                          @Qualifier("studentsGenerationData") GenerationData<Student> studentsGenerationData) {
        this.studentsDAO = studentsDAO;
        this.enrollInfoGenerationData = enrollInfoGenerationData;
        this.studentGenerationData = studentsGenerationData;
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

    public void enrollBatchStudentInCourse(List<EnrollInfo> enrollInfo){
        studentsDAO.enrollBatchStudentInCourse(enrollInfo);
    }

    public boolean removeStudentFromCourse(int studentId, int courseId) {
        return studentsDAO.removeStudentFromCourse(studentId, courseId);
    }

    public List<Student> getStudentsByCourseName(String courseName) {
        return studentsDAO.findStudentsByCourseName(courseName);
    }

    @Override
    public void generateDataAndPopulateDB(){
        createMultipleStudents(studentGenerationData.generateData());
        List<EnrollInfo> generationEnroll = enrollInfoGenerationData.generateData();

        enrollBatchStudentInCourse(generationEnroll);
    }
}
