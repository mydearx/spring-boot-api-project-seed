package com.smart.project.annotation;

import org.slf4j.event.Level;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    //操作类型，用enum去定义
    String operateType() default "";
    //是否控制台打印
    boolean console() default true;
    //描述
    String description() default "";
}
