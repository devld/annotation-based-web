package ld.common.request;

import ld.common.context.Context;

import java.lang.annotation.Annotation;

public interface ParameterResolver {

    /**
     * 解析 request 的方法参数
     *
     * @param type        参数类型
     * @param annotations 参数的注解
     * @return object
     */
    Object resolveParameter(Context context, Class<?> type, Annotation[] annotations) throws ParameterResolveException;

    /**
     * 是否支持改参数类型
     *
     * @param type        参数类型
     * @param annotations 参数的注解
     * @return supported
     */
    boolean supported(Class<?> type, Annotation[] annotations);

}
