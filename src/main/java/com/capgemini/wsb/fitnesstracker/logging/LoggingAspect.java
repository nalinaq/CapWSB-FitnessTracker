package com.capgemini.wsb.fitnesstracker.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Calling method: " + joinPoint.getSignature().getName());
    }

    @After("execution(* com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Method executed: " + joinPoint.getSignature().getName());
    }
}
