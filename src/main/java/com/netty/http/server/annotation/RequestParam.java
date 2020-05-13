package com.netty.http.server.annotation;

import com.netty.http.server.common.Constant;

import java.lang.annotation.*;

@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestParam {

    String name();

    boolean required() default true;

    String defaultValue() default Constant.EMPTY;
}
