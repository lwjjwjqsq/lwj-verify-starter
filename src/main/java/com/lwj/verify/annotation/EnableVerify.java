package com.lwj.verify.annotation;


import com.lwj.verify.config.VerifyAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(VerifyAutoConfig.class)
public @interface EnableVerify {
}
