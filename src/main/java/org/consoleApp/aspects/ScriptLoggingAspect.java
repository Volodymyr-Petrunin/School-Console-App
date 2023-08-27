package org.consoleApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ScriptLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(ScriptLoggingAspect.class);
    private final CreateLoggerInfo createLoggerInfo = new CreateLoggerInfo(logger);

    @Pointcut("execution(* org.consoleApp.dataBaseSettings.ScriptRunner.runScript(..))")
    public void runScriptMethod() {}

    @Before("runScriptMethod()")
    public void beforeRunScript(JoinPoint joinPoint){
        if (logger.isInfoEnabled()){
            logger.info("Started using method {} in class {}",
                    joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
        }
    }

    @AfterThrowing(pointcut = "runScriptMethod()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex){
        createLoggerInfo.createErrorString(joinPoint, ex);
    }
}
