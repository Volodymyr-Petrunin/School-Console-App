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
    private final CreateLoggerInfo createLoggerInfo = new CreateLoggerInfo(logger);
    private static final String INFO = "Started using method {} in class {}";

    @Pointcut("execution(* org.consoleApp.dao.CourseDAO.*(..))")
    public void coursesDAOMethods(){}

    @Pointcut("execution(* org.consoleApp.dao.GroupDAO.*(..))")
    public void groupsDAOMethods(){}

    @Pointcut("execution(* org.consoleApp.dao.StudentsDAO.*(..))")
    public void studentsDAOMethods(){}

    @Before("coursesDAOMethods()")
    public void beforeCoursesDAOMethodExecution(JoinPoint joinPoint){
        if (logger.isInfoEnabled()) {
            logger.info(INFO, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
        }
    }

    @Before("groupsDAOMethods()")
    public void beforeGroupsDAOMethodExecution(JoinPoint joinPoint){
        if (logger.isInfoEnabled()) {
            logger.info(INFO, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
        }
    }

    @Before("studentsDAOMethods()")
    public void beforeStudentsDAOMethodExecution(JoinPoint joinPoint){
        if (logger.isInfoEnabled()) {
            logger.info(INFO, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "list")
    public void coursesListReturningAdvice(JoinPoint joinPoint, List<?> list){
        if (logger.isInfoEnabled()) {
            createLoggerInfo.createListLogInfoAfterReturning(joinPoint, list);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "optional")
    public void courseOptionalReturningAdvice(JoinPoint joinPoint, Optional<?> optional){
        if (logger.isInfoEnabled()) {
            createLoggerInfo.createOptionalLogInfoAfterReturning(joinPoint, optional);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "result")
    public void courseBooleanReturningAdvice(JoinPoint joinPoint, boolean result){
        if (logger.isInfoEnabled()) {
            createLoggerInfo.createBooleanLogInfoAfterReturning(joinPoint, result);
        }
    }

    @AfterThrowing(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex){
        if (logger.isErrorEnabled()) {
            createLoggerInfo.createErrorString(joinPoint, ex);
        }
    }
}
