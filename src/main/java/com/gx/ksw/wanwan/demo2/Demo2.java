package com.gx.ksw.wanwan.demo2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Demo2 {
    /**
     * 调用说明
     */
    String value() default "";
}
