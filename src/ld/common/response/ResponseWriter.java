package ld.common.response;

import ld.common.context.Context;

import java.io.IOException;

public interface ResponseWriter {

    /**
     * 将返回的结果写出
     *
     * @param result     result
     * @param resultType result type
     * @param context    context
     */
    void write(Object result, Class<?> resultType, Context context) throws IOException;


    /**
     * 是否支持该类型的结果
     *
     * @param resultType result type
     * @return supported
     */
    boolean supported(Class<?> resultType);

}
