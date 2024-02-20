package ru.clevertec.logginstarter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
public class CustomLogAspect {
    private static Logger logger = Logger.getLogger("InfoLogging");

    @Pointcut("execution(* * (..))")
    public void everyMethod() {
    }

    @Pointcut("within(@ru.clevertec.logginstarter.annotation.CustomMethodLog *)")
    public void classAnnotated() {
    }

    @Around(value = "classAnnotated() && everyMethod()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(joinPoint.getArgs());

        StringBuilder logMessage = new StringBuilder("\nLog info: \n")
                .append(joinPoint.getTarget().getClass().getSimpleName())
                .append(".")
                .append(joinPoint.getSignature().getName())
                .append("\nargs: ")
                .append(Arrays.toString(joinPoint.getArgs()))
                .append("\nreturn: ")
                .append(result);
        logger.info(logMessage.toString());

        return result;
    }

}
