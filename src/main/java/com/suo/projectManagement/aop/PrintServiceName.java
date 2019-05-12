package com.suo.projectManagement.aop;

import java.lang.annotation.*;

/**
 * Created by Suo Tian on 2018-05-17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PrintServiceName {
    String description() default "";
}
