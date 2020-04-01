package com.borchowiec.coronavirustrackerforpoland.aspect;

import com.borchowiec.coronavirustrackerforpoland.service.HistoryService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HistoryAspect {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Logs {@link HistoryService#updateHistoryList()} method.
     * @param proceedingJoinPoint
     */
    @Around("execution(* com.borchowiec.coronavirustrackerforpoland.service.HistoryService.updateHistoryList())")
    public void updateHistoryListMethod(ProceedingJoinPoint proceedingJoinPoint){
        logger.info("Starting updating history list...");
        boolean success = true;

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Cannot update history list: " + throwable.getMessage());
            success = false;
        }

        if (success) {
            logger.info("History list has been updated");
        }
    }
}
