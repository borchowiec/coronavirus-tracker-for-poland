package com.borchowiec.coronavirustrackerforpoland.aspect;

import com.borchowiec.coronavirustrackerforpoland.service.RegionalDataService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RegionalDataAspect {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Logs {@link RegionalDataService#updateRegionalData()} ()} method.
     * @param proceedingJoinPoint
     */
    @Around("execution(* com.borchowiec.coronavirustrackerforpoland.service.RegionalDataService.updateRegionalData())")
    public void updateRegionalDataMethod(ProceedingJoinPoint proceedingJoinPoint){
        logger.info("Starting updating regional data...");
        boolean success = true;

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Cannot update regional data: " + throwable.getMessage());
            success = false;
        }

        if (success) {
            logger.info("Regional data has been updated.");
        }
    }
}
