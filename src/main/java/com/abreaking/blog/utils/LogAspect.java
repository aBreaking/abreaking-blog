package com.abreaking.blog.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.abreaking.blog.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuilder logInfo = new StringBuilder();
        logInfo.append("Request->");
        logInfo.append("REQUEST_METHOD:").append(request.getMethod());
        logInfo.append(",");
        logInfo.append("URL:").append(request.getRequestURL());
        logInfo.append(",");
        logInfo.append("IP:").append(IPKit.getIpAddrByRequest(request));
        logInfo.append(",");
        logInfo.append("CLASS_METHOD:").append(joinPoint.getSignature().getDeclaringTypeName()).append(".").append(joinPoint.getSignature().getName());
        logInfo.append(",");
        logInfo.append("ARGS:").append(Arrays.toString(joinPoint.getArgs()));

        // 记录下请求内容
        LOGGER.info(logInfo.toString());
    }

    @AfterReturning(returning = "object", pointcut = "webLog()")
    public void doAfterReturning(Object object) {
        // 处理完请求，返回内容
        LOGGER.info("RESPONSE->" + object);
    }
}
