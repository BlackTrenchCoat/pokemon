package org.theproject.y2012.pokemon;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLogging {

    @Around("execution(* org.theproject.y2012.pokemon..*.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {

      String signature = joinPoint.getSignature().getName();
      String methodName;

      if (joinPoint.getTarget() == null) {
//          System.out.println("joinPoint.getTarget() == null");
          methodName = signature + "()";
      } else {
//          System.out.println("joinPoint.getTarget() != null");
          String className = joinPoint.getTarget().getClass().getName();
          methodName = className + "." + signature + "()";
      }

      Logger.getLogger(this.getClass())
              .info("Entering " + methodName + "...");

      Object result = joinPoint.proceed();

      Logger.getLogger(this.getClass()).info("Leaving " + methodName + ".");

      return result;
  }
}
