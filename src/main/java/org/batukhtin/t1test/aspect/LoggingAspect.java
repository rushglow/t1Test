package org.batukhtin.t1test.aspect;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.batukhtin.t1test.model.UserEntity;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("@annotation(org.batukhtin.t1test.aspect.annotation.LogExecution)")
    public void loggingBefore(JoinPoint joinPoint) {
        log.info("Called method: " + joinPoint.getSignature().getName() + "with parameters: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(
            pointcut = "@annotation(org.batukhtin.t1test.aspect.annotation.LogException)",
            throwing = "exception"
    )
    public void loggingAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("Exception in method" + joinPoint.getSignature().getName() + " with parametr: " + Arrays.toString(joinPoint.getArgs()));
        log.error("Exception is : " + exception.getClass().getName() + " with message: " + exception.getMessage());
    }

    @AfterReturning(
            pointcut = "@annotation(org.batukhtin.t1test.aspect.annotation.HandlingResult)",
            returning = "result"
    )
    public void afterReturning(JoinPoint joinPoint, UserEntity result) {
        log.info("Method: " + joinPoint.getSignature().getName() + " with parameters: " + Arrays.toString(joinPoint.getArgs()));
        log.info("User registred with id=" + result.getId() + " and username=" + result.getUsername());
    }

    @Around("@annotation(org.batukhtin.t1test.aspect.annotation.PerfomanceTracking)")
    public Object loggingAround(ProceedingJoinPoint joinPoint)  {
        Object proceed;

        log.info("Perfomance test START: " + joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();

        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException("Aspect failed");
        }

        long end = System.currentTimeMillis();

        log.info("Perfomance test FINISH: " + joinPoint.getSignature().getName());
        log.info("Time of perfomans test: " + (end - start));
        return proceed;
    }
}
