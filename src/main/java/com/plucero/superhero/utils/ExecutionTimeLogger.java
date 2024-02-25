package com.plucero.superhero.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeLogger {

    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        final long startTime = System.currentTimeMillis();
        final Object result = joinPoint.proceed();
        final long endTime = System.currentTimeMillis();
        final long executionTime = endTime - startTime;
        final String methodName = joinPoint.getSignature().toShortString();

        final String description = logExecutionTime.value().isEmpty() ? methodName : logExecutionTime.value();

        log.info("{} service executed in {} ms", description, executionTime);

        return result;
    }
}
