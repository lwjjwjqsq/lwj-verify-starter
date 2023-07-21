package com.lwj.verify.aop;

import cn.hutool.extra.spring.SpringUtil;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lwj.verify.annotation.Value;
import com.lwj.verify.annotation.Verify;
import com.lwj.verify.inter.GetTokenInterface;
import com.lwj.verify.property.VerifyProperty;
import com.lwj.verify.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.NotActiveException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;


@Aspect
@AllArgsConstructor
public class VerifyAOP {

    private final JWTUtil jwtUtil;

    @Around(value = "@annotation(com.lwj.verify.annotation.Verify)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Verify verify = method.getAnnotation(Verify.class);
        HttpServletRequest request = sra.getRequest();
        String token = SpringUtil.getBean(verify.tokenProvider()).getToken(request);
        if (token == null) {
            return notLogin(joinPoint);
        }
        // 体验token
        DecodedJWT decodedJWT;
        try {
            decodedJWT = jwtUtil.getToken(token);
        } catch (Exception e) {
            return notLogin(joinPoint);
        }
        Object[] args = getArgs(joinPoint, decodedJWT);
        return joinPoint.proceed(args);
    }

    private Object[] getArgs(ProceedingJoinPoint joinPoint, DecodedJWT token) throws Throwable {
        // 插入参数
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            return args;
        }
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] paramAnn = annotations[i];
            //参数为空，直接下一个参数
            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为Value.class
                if (!annotation.annotationType().equals(Value.class)) {
                    continue;
                }
                String key = ((Value) annotation).value();
                Claim claim = token.getClaim(key);
                if (claim == null) {
                    throw new RuntimeException("当前没有这key:'"+key+"'对应的值");
                }
                args[i] = claim.as(parameterTypes[i]);
            }
        }
        return args;
    }

    private static Object notLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class<?> classTarget = joinPoint.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, par);
        Verify annotation = objMethod.getAnnotation(Verify.class);
        if (annotation.required()) {
            return SpringUtil.getBean(annotation.noLoginProvider()).notLogin(joinPoint);
        } else {
            return joinPoint.proceed();
        }
    }

}
