package com.lwj.verify.inter;

import org.aspectj.lang.ProceedingJoinPoint;

import java.io.NotActiveException;


public class NotLoginInterfaceDefault implements NotLoginInterface {
    @Override
    public Object notLogin(ProceedingJoinPoint joinPoint) throws NotActiveException {
        throw new NotActiveException();
    }
}
