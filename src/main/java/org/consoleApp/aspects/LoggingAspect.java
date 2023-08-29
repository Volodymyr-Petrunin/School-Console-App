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
    private static final String BEFORE_METHOD = "Started using method {} in class {}";
    private static final String RETURNING_LIST = "Method {} in class {} returned list with size: {}";
    private static final String RETURNING_BOOLEAN = "Method {} in class {} returned: {}";
    private static final String RETURNING_OPTIONAL = "Method {} in class {} returned optional: {}";
    private static final String RETURNING_OPTIONAL_EMPTY = "Method {} in class {} returned empty optional";
    private static final String RETURNING_COURSE_OBJECT = "Method {} in class {} returned Course object: {}";
    private static final String EXCEPTION = "Exception occurred in method: {} in class: {}";

    @Pointcut("execution(* org.consoleApp.dao.*.*(..))")
    public void methodsDAO(){}
    @Pointcut("execution(* org.consoleApp.generation.impl.*.*(..))")
    public void generatorService(){}
    @Pointcut("execution(* org.consoleApp.readers.ResourcesFileReader.*(..))")
    public void resourcesFileReader(){}
    @Pointcut("execution(* org.consoleApp.parser.impl.CourseParser.*(..))")
    public void courseParser(){}


    @Before("methodsDAO() || generatorService() || resourcesFileReader() || courseParser()")
    public void beforeMethodExecution(JoinPoint joinPoint){
        if (logger.isDebugEnabled()) {
            logger.debug(BEFORE_METHOD, getMethodName(joinPoint), getClassName(joinPoint));
        }
    }

    @AfterReturning(pointcut = "methodsDAO() || generatorService() || resourcesFileReader()", returning = "list")
    public void listReturningAdvice(JoinPoint joinPoint, List<?> list){
        if (logger.isDebugEnabled()) {
            logger.debug(RETURNING_LIST, getMethodName(joinPoint), getClassName(joinPoint), list.size());
        }
    }

    @AfterReturning(pointcut = "methodsDAO()", returning = "optional")
    public void optionalReturningAdvice(JoinPoint joinPoint, Optional<?> optional){
        if (logger.isDebugEnabled()) {
            if (optional.isPresent()){
                logger.debug(RETURNING_OPTIONAL, getMethodName(joinPoint), getClassName(joinPoint), optional.get().toString());
            }else {
                logger.debug(RETURNING_OPTIONAL_EMPTY, getMethodName(joinPoint), getClassName(joinPoint));
            }
        }
    }

    @AfterReturning(pointcut = "methodsDAO()", returning = "result")
    public void booleanReturningAdvice(JoinPoint joinPoint, boolean result){
        if (logger.isDebugEnabled()) {
            logger.debug(RETURNING_BOOLEAN, getMethodName(joinPoint), getClassName(joinPoint), result);
        }
    }

    @AfterReturning(pointcut = "courseParser()", returning = "course")
    public void courseReturningAdvice(JoinPoint joinPoint, Course course){
        if (logger.isDebugEnabled()){
            logger.debug(RETURNING_COURSE_OBJECT, getMethodName(joinPoint), getClassName(joinPoint), course.toString());
        }
    }

    @AfterThrowing(pointcut = "methodsDAO() || generatorService() || resourcesFileReader() || courseParser()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex){
        if (logger.isErrorEnabled()) {
            logger.error(EXCEPTION, getMethodName(joinPoint), getClassName(joinPoint), ex);
        }
    }

    private String getMethodName(JoinPoint joinPoint){
        return joinPoint.getSignature().getName();
    }

    private String getClassName(JoinPoint joinPoint){
        return joinPoint.getTarget().getClass().getName();
    }
}
