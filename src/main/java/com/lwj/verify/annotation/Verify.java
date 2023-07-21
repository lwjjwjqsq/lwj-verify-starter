package com.lwj.verify.annotation;

import com.lwj.verify.inter.GetTokenInterface;
import com.lwj.verify.inter.GetTokenInterfaceDefault;
import com.lwj.verify.inter.NotLoginInterface;
import com.lwj.verify.inter.NotLoginInterfaceDefault;

import java.lang.annotation.*;

/**
 * jwt校验
 *
 * 当方法标注了此直接则会使用jwt校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Verify {
    /**
     * 是否必须校验， 默认是true
     */
    boolean required() default true;

    /**
     * 获取token方法实现类
     */
    Class<? extends GetTokenInterface> tokenProvider() default GetTokenInterfaceDefault.class;

    /**
     * 没有登录方法实现类
     */
    Class<? extends NotLoginInterface> noLoginProvider() default NotLoginInterfaceDefault.class;
}
