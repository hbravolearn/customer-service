package com.company.ecommerce.customer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static com.company.ecommerce.customer.constant.CommonConstant.MAX_ARRAY_SIZE;
import static com.company.ecommerce.customer.constant.CommonConstant.MAX_LIST_SIZE;
import static com.company.ecommerce.customer.constant.CommonConstant.MAX_STRING_LENGTH;

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
        String args = Arrays.toString(joinPoint.getArgs());

        Instant startTime = Instant.now();
        try {
            log.info("Entering method: {}.{}() with arguments: {}", className, methodName, args);
            Object result = joinPoint.proceed();
            Object truncatedResult = truncateResult(result);
            log.info("Exiting method: {}.{}() with result: {}", className, methodName, truncatedResult);

            return result;
        } catch (Exception ex) {
            log.error("Error in method: {}.{}()", className, methodName, ex);
            throw ex;
        } finally {
            Instant endTime = Instant.now();
            Duration duration = Duration.between(startTime, endTime);
            log.info("Total execution time of {}.{}(): {} ms", className, methodName, duration.toMillis());
        }
    }

    private Object truncateResult(Object result) {
        return switch (result) {
            case String stringResult ->
                    stringResult.length() > MAX_STRING_LENGTH ? stringResult.substring(0, MAX_STRING_LENGTH)
                            .concat("...") : stringResult;
            case List<?> listResult ->
                    listResult.size() > MAX_LIST_SIZE ? listResult.subList(0, MAX_LIST_SIZE): listResult;
            case Object[] arrayResult ->
                    arrayResult.length > MAX_ARRAY_SIZE ? Arrays.copyOf(arrayResult, MAX_ARRAY_SIZE) : arrayResult;
            default -> result;
        };
    }

}
