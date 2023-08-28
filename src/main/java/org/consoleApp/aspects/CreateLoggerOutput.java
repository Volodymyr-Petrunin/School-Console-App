package org.consoleApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.consoleApp.domin.Course;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class CreateLoggerOutput {
    private final Logger logger;

    public CreateLoggerOutput(Logger logger) {
        this.logger = logger;
    }

    public void createErrorString(JoinPoint joinPoint, Exception exception){
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        String error = new StringJoiner(System.lineSeparator())
                .add("Exception occurred in method: " + methodName + "in class: " + className)
                .add("Exception type: " + exception.getClass().getName())
                .add("Exception message: " + exception.getMessage())
                .toString();

        logger.error(error);
    }

    public void createListLogInfoAfterReturning(JoinPoint joinPoint, List<?> list){
            logger.debug("Method {} in class {} returned list with size: {}",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName(),
                    list.size());
    }

    public void createOptionalLogInfoAfterReturning(JoinPoint joinPoint, Optional<?> optional){
        if (optional.isPresent()){
            logger.debug("Method {} in class {} returned optional: {}",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName(),
                    optional.get().toString());
        }else {
            logger.debug("Method {} in class {} returned empty optional",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName());
        }
    }

    public void createBooleanLogInfoAfterReturning(JoinPoint joinPoint, boolean result){
        logger.debug("Method {} in class {} returned: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getName(),
                result);
    }

    public void createCourseLogInfoAfterReturning(JoinPoint joinPoint, Course course){
        if (course != null){
            logger.debug("Method {} in class {} returned Course object: {}",
                    joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getName(),
                    course.toString());
        }
    }
}
