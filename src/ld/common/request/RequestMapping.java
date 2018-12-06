package ld.common.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestMapping {

    /**
     * url path
     */
    String value() default "";

    /**
     * 请求方法
     */
    HttpMethod[] method() default {};

}
