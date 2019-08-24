package com.terrier.finances.gestion.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class IHMAspect {
 
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IHMAspect.class);
	
	@Around("@annotation(LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		 
	    Object proceed = joinPoint.proceed();
	 
	    long executionTime = System.currentTimeMillis() - start;
	 
	    LOGGER.info("****" + joinPoint.getSignature() + " executed in " + executionTime + "ms");
	    return proceed;
	}
}
