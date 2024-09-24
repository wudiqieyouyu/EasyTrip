package com.easytrip.annotation;

import com.easytrip.entity.enums.RequestFrequencyTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {
    boolean checkParams() default true;

    boolean checkLogin() default false;

    int requestFrequencyThreshold() default 0;

    RequestFrequencyTypeEnum frequencyType() default RequestFrequencyTypeEnum.NO_LIMIT;
}
