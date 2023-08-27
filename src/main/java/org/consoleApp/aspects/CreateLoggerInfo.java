package org.consoleApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class CreateLoggerInfo {
    private final Logger logger;

    public CreateLoggerInfo(Logger logger) {
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

    public void createOptionalLogInfoAfterReturning(JoinPoint joinPoint, Optional<?> optional){
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

    public void createBooleanLogInfoAfterReturning(JoinPoint joinPoint, boolean result){
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
