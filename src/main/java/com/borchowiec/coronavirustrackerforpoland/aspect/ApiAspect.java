package com.borchowiec.coronavirustrackerforpoland.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiAspect {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * All methods inside {@link com.borchowiec.coronavirustrackerforpoland.controller.ApiController ApiController} class.
     */
    @Pointcut("execution(* com.borchowiec.coronavirustrackerforpoland.controller.ApiController.*(..))")
    private void allApiMethods() {}

    /**
     * Logs calls of all methods from api
     * {@link com.borchowiec.coronavirustrackerforpoland.controller.ApiController ApiController} class.
     * @param joinPoint Contains info about method.
     */
    @Before("allApiMethods()")
    public void logMethodCalls(JoinPoint joinPoint){
        logger.info("Starting " + joinPoint.getSignature().toShortString());
    }

    /**
     * Logs exceptions of all methods from api
     * {@link com.borchowiec.coronavirustrackerforpoland.controller.ApiController ApiController} class.
     * @param joinPoint Contains info about method.
     */
    @AfterThrowing(value = "allApiMethods()", throwing = "e")
    public void logThrowingExceptions(JoinPoint joinPoint, Exception e) {
        logger.error(joinPoint.getSignature().toShortString() + ": " + e.getMessage());
    }
}
