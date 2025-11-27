package com.example.GreenGrub.aspect;

import com.example.GreenGrub.services.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private LogService logService;

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            logService.sendApplicationLog("INFO",
                    "Controller method executed: " + methodName,
                    Map.of("executionTime", executionTime, "method", methodName)
            );

            return result;
        } catch (Exception e) {
            logService.sendApplicationLog("ERROR",
                    "Controller method failed: " + methodName + " - " + e.getMessage(),
                    Map.of("method", methodName, "error", e.getClass().getSimpleName())
            );
            throw e;
        }
    }
}