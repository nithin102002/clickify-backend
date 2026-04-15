package com.example.userservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Target all service layer methods
    @Pointcut("execution(* com.example.userservice.service..*(..))")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        log.info("➡️ Entering: {}",
                joinPoint.getSignature().toShortString());

        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - start;

        log.info("⬅️ Exiting: {} | Time Taken: {} ms",
                joinPoint.getSignature().toShortString(),
                timeTaken);

        return result;
    }

    // Log exceptions
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {

        log.error("❌ Exception in {} : {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage());
    }
}
