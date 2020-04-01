package com.borchowiec.coronavirustrackerforpoland.aspect;

import com.borchowiec.coronavirustrackerforpoland.service.HistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiAspect {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut("execution(* com.borchowiec.coronavirustrackerforpoland.controller.ApiController.*(..))")
    private void allApiMethods() {}

    @Before("allApiMethods()")
    public void logMethodCalls(JoinPoint joinPoint){
        logger.info("Starting " + joinPoint.getSignature().toShortString());
    }

    @AfterThrowing(value = "allApiMethods()", throwing = "e")
    public void logThrowingExceptions(JoinPoint joinPoint, Exception e) {
        logger.error(joinPoint.getSignature().toShortString() + ": " + e.getMessage());
    }
}
