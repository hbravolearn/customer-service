package com.company.ecommerce.customer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.company.ecommerce.customer.service.impl.*.*(..))")
    public void publicMethodsInServicePackage() {

    }

    @Around("publicMethodsInServicePackage()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Entering method: {}.{}() with arguments: {}", className, methodName, args);
        Instant startTime = Instant.now();

        try {
            Object result = joinPoint.proceed();
            Instant endTime = Instant.now();
            Duration duration = Duration.between(startTime, endTime);
            log.info("Exiting method: {}.{}() with result: {}. Time taken: {} ms", className, methodName, result,
                    duration.toMillis());
            return result;
        } catch (Exception ex) {
            log.error("Error in method: {}.{}()", className, methodName, ex);
            throw ex;
        }
    }

}