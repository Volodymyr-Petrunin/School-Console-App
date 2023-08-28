package org.consoleApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.consoleApp.domin.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final CreateLoggerOutput createLoggerOutput = new CreateLoggerOutput(logger);
    private static final String DEBUG = "Started using method {} in class {}";

    @Pointcut("execution(* org.consoleApp.dao.CourseDAO.*(..))")
    public void coursesDAOMethods(){}

    @Pointcut("execution(* org.consoleApp.dao.GroupDAO.*(..))")
    public void groupsDAOMethods(){}

    @Pointcut("execution(* org.consoleApp.dao.StudentsDAO.*(..))")
    public void studentsDAOMethods(){}
    @Pointcut("execution(* org.consoleApp.generation.impl.CoursesGeneratorService.*(..))")
    public void coursesGeneratorService(){}
    @Pointcut("execution(* org.consoleApp.generation.impl.EnrollmentsDataGeneration.*(..))")
    public void enrollmentsDataGeneration(){}
    @Pointcut("execution(* org.consoleApp.generation.impl.GroupGenerationData.*(..))")
    public void groupGenerationData(){}
    @Pointcut("execution(* org.consoleApp.generation.impl.StudentsGenerationData.*(..))")
    public void studentsGenerationData(){}
    @Pointcut("execution(* org.consoleApp.generation.impl.StudentsGeneratorService.*(..))")
    public void studentsGeneratorService(){}
    @Pointcut("execution(* org.consoleApp.readers.ResourcesFileReader.*(..))")
    public void resourcesFileReader(){}
    @Pointcut("execution(* org.consoleApp.parser.impl.CourseParser.*(..))")
    public void courseParser(){}


    @Before("coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods() || coursesGeneratorService() " +
            "|| enrollmentsDataGeneration() || groupGenerationData() || studentsGenerationData() " +
            "|| studentsGeneratorService() || resourcesFileReader() || courseParser()")
    public void beforeMethodExecution(JoinPoint joinPoint){
        if (logger.isDebugEnabled()) {
            logger.debug(DEBUG, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods() " +
            "|| coursesGeneratorService() || enrollmentsDataGeneration() || groupGenerationData() " +
            "|| studentsGenerationData() || studentsGeneratorService() || resourcesFileReader()", returning = "list")
    public void listReturningAdvice(JoinPoint joinPoint, List<?> list){
        if (logger.isDebugEnabled()) {
            createLoggerOutput.createListLogInfoAfterReturning(joinPoint, list);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "optional")
    public void optionalReturningAdvice(JoinPoint joinPoint, Optional<?> optional){
        if (logger.isDebugEnabled()) {
            createLoggerOutput.createOptionalLogInfoAfterReturning(joinPoint, optional);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "result")
    public void booleanReturningAdvice(JoinPoint joinPoint, boolean result){
        if (logger.isDebugEnabled()) {
            createLoggerOutput.createBooleanLogInfoAfterReturning(joinPoint, result);
        }
    }

    @AfterReturning(pointcut = "courseParser()", returning = "course")
    public void courseReturningAdvice(JoinPoint joinPoint, Course course){
        if (logger.isDebugEnabled()){
            createLoggerOutput.createCourseLogInfoAfterReturning(joinPoint, course);
        }
    }

    @AfterThrowing(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods() " +
            "|| coursesGeneratorService() || enrollmentsDataGeneration() || groupGenerationData() " +
            "|| studentsGenerationData() || studentsGeneratorService() || resourcesFileReader() " +
            "|| courseParser()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex){
        if (logger.isErrorEnabled()) {
            createLoggerOutput.createErrorString(joinPoint, ex);
        }
    }
}
