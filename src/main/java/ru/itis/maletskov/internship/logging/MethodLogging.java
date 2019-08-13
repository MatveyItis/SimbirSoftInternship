package ru.itis.maletskov.internship.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLogging {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodLogging.class);

    @Before(value = "execution(* ru.itis.maletskov.internship.controller..*.*(..))")
    public void beforeControllerMethods(JoinPoint joinPoint) {
        LOGGER.info("Controller's method '" + joinPoint.getSignature().getName() + "' has started");
    }

    @After(value = "execution(* ru.itis.maletskov.internship.controller..*.*(..))")
    public void afterControllerMethods(JoinPoint joinPoint) {
        LOGGER.info("Controller's method '" + joinPoint.getSignature().getName() + "' finished its execution");
    }

    @Before(value = "execution(* ru.itis.maletskov.internship.service..*.*(..))")
    public void beforeServiceMethods(JoinPoint joinPoint) {
        LOGGER.info("Service method '" + joinPoint.getSignature().getName() + "' has started");
    }

    @After(value = "execution(* ru.itis.maletskov.internship.service..*.*(..))")
    public void afterServiceMethods(JoinPoint joinPoint) {
        LOGGER.info("Service method '" + joinPoint.getSignature().getName() + "' finished its execution");
    }
}
