package com.xyoye.dandanplay2.utils.interf.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by xyy on 2017/6/23.
 */
@Documented
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface DHeader {
    String value();
}
