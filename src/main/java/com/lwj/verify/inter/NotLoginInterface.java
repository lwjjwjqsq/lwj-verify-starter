package com.lwj.verify.inter;


import org.aspectj.lang.ProceedingJoinPoint;

public interface NotLoginInterface {
    Object notLogin(ProceedingJoinPoint joinPoint) throws Exception;
}
