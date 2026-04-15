package com.example.product_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.example.product_service.service..*(..))")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        log.info("Entering: {}", joinPoint.getSignature().toShortString());

        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - start;

        log.info("Exiting: {} | Time: {} ms",
                joinPoint.getSignature().toShortString(),
                timeTaken);

        return result;
    }
}