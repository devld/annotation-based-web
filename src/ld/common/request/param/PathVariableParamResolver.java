package ld.common.request.param;

import ld.common.context.Context;
import ld.common.request.ParameterResolveException;
import ld.common.request.ParameterResolver;

import java.lang.annotation.Annotation;
import java.util.Map;

public class PathVariableParamResolver implements ParameterResolver {
    @Override
    public Object resolveParameter(Context context, Class<?> type, Annotation[] annotations) throws ParameterResolveException {
        Map<String, String> pathParams = context.getRoute()
                .parseUrlParams(context.getHttpServletRequest().getRequestURI());
        PathVariable annotation = null;
        for (Annotation a : annotations) {
            if (a instanceof PathVariable) {
                annotation = (PathVariable) a;
                break;
            }
        }
        assert annotation != null;
        String value = pathParams.get(annotation.value());
        if (value == null) {
            return null;
        }
        if (type == String.class) {
            return value;
        }
        try {
            if (type == Integer.class) {
                return Integer.valueOf(value);
            }
            if (type == Integer.TYPE) {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            throw new ParameterResolveException("invalid value: " + value);
        }
        return null;
    }

    @Override
    public boolean supported(Class<?> type, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof PathVariable) {
                return true;
            }
        }
        return false;
    }
}
