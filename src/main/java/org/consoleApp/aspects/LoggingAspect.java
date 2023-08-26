package org.consoleApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
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
            createListLogInfoAfterReturning(joinPoint, list);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "optional")
    public void courseOptionalReturningAdvice(JoinPoint joinPoint, Optional<?> optional){
        if (logger.isInfoEnabled()) {
            createOptionalLogInfoAfterReturning(joinPoint, optional);
        }
    }

    @AfterReturning(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", returning = "result")
    public void courseBooleanReturningAdvice(JoinPoint joinPoint, boolean result){
        if (logger.isInfoEnabled()) {
            createBooleanLogInfoAfterReturning(joinPoint, result);
        }
    }

    @AfterThrowing(pointcut = "coursesDAOMethods() || groupsDAOMethods() || studentsDAOMethods()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex){
        logger.error(createErrorString(joinPoint, ex));
    }

    private String createErrorString(JoinPoint joinPoint, Exception exception){
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        return new StringJoiner(System.lineSeparator())
                .add("Exception occurred in method: " + methodName + "in class: " + className)
                .add("Exception type: " + exception.getClass().getName())
                .add("Exception message: " + exception.getMessage())
                .toString();
    }

    private void createListLogInfoAfterReturning(JoinPoint joinPoint, List<?> list){
        if (!list.isEmpty()){
            logger.info("Method {} in class {} returned list with size: {}",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName(),
                    list.size());
        } else {
            logger.info("Method {} in class {} returned empty list",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName());
        }
    }

    private void createOptionalLogInfoAfterReturning(JoinPoint joinPoint, Optional<?> optional){
        if (optional.isPresent()){
            logger.info("Method {} in class {} returned optional of course: {}",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName(),
                    optional.get().toString());
        }else {
            logger.info("Method {} in class {} returned empty optional",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName());
        }
    }

    private void createBooleanLogInfoAfterReturning(JoinPoint joinPoint, boolean result){
        if (result){
            logger.info("Method {} in class {} returned: true",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName());
        }else {
            logger.info("Method {} in class {} returned: false",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName());
        }
    }
}
