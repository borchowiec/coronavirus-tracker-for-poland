package com.borchowiec.coronavirustrackerforpoland.aspect;

import com.borchowiec.coronavirustrackerforpoland.service.NewsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NewsAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Logs {@link NewsService#updateNews()} ()} ()} method.
     * @param proceedingJoinPoint
     */
    @Around("execution(* com.borchowiec.coronavirustrackerforpoland.service.RegionalDataService.updateRegionalData())")
    public void updateNewsMethod(ProceedingJoinPoint proceedingJoinPoint){
        logger.info("Starting updating news...");
        boolean success = true;

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Cannot update news: " + throwable.getMessage());
            success = false;
        }

        if (success) {
            logger.info("News has been updated.");
        }
    }

}
