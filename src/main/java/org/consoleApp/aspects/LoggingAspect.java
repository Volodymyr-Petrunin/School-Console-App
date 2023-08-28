package org.consoleApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
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


    @Before("coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()")
    public void beforeCoursesDAOMethodExecution(JoinPoint joinPoint){
        if (logger.isDebugEnabled()) {
            logger.debug(DEBUG, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "list")
    public void coursesListReturningAdvice(JoinPoint joinPoint, List<?> list){
        if (logger.isInfoEnabled()) {
            createLoggerOutput.createListLogInfoAfterReturning(joinPoint, list);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "optional")
    public void courseOptionalReturningAdvice(JoinPoint joinPoint, Optional<?> optional){
        if (logger.isInfoEnabled()) {
            createLoggerOutput.createOptionalLogInfoAfterReturning(joinPoint, optional);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "result")
    public void courseBooleanReturningAdvice(JoinPoint joinPoint, boolean result){
        if (logger.isInfoEnabled()) {
            createLoggerOutput.createBooleanLogInfoAfterReturning(joinPoint, result);
        }
    }

    @AfterThrowing(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex){
        if (logger.isErrorEnabled()) {
            createLoggerOutput.createErrorString(joinPoint, ex);
        }
    }
}
