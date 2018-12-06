package ld.common.request;

import ld.common.context.Context;

import java.lang.annotation.Annotation;

public class ContextParamResolver implements ParameterResolver {
    @Override
    public Object resolveParameter(Context context, Class<?> type, Annotation[] annotations) {
        return context;
    }

    @Override
    public boolean supported(Class<?> type, Annotation[] annotations) {
        return type == Context.class;
    }
}
