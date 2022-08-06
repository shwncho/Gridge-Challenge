package com.server.insta.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class Logger {

    private final LogService logService;

    @Around("execution(* com.server.insta.service.*.*(..))" +
            "&& !@annotation(com.server.insta.log.NoLogging)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("함수명: " + joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature().getName());
        sb.append("\n");
        sb.append("매개변수: ");

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object o : args) {
                sb.append(o);
                sb.append("리턴값: " + result);
            }
            logService.add(sb.toString());
            log.info(sb.toString());
        }
        return result;
    }

}
