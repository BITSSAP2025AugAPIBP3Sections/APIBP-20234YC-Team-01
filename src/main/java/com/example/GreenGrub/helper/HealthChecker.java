package com.example.GreenGrub.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HealthChecker {
    public static Logger logger = LoggerFactory.getLogger(HealthChecker.class);

    // setting  a cron job for health check which will run every 5 minute interval
    @Scheduled(cron = "0 * * * * ?")
    public void logHealthStatus() {
        logger.info("HEALTH_CHECK_LOG: Application is running and performing its routine health check.");
    }
}
