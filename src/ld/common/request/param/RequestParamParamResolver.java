package ld.common.request.param;

import ld.common.context.Context;
import ld.common.request.ParameterResolveException;
import ld.common.request.ParameterResolver;
import ld.common.util.Utils;

import java.lang.annotation.Annotation;

public class RequestParamParamResolver implements ParameterResolver {
    @Override
    public Object resolveParameter(Context context, Class<?> type, Annotation[] annotations) throws ParameterResolveException {
        RequestParam annotation = null;
        for (Annotation a : annotations) {
            if (a instanceof RequestParam) {
                annotation = (RequestParam) a;
                break;
            }
        }
        assert annotation != null;
        String value = context.getHttpServletRequest().getParameter(annotation.value());
        try {
            return Utils.parseStringToObject(value, type);
        } catch (IllegalArgumentException e) {
            if (annotation.required()) {
                throw new ParameterResolveException("invalid value: " + value);
            }
        }
        return null;
    }

    @Override
    public boolean supported(Class<?> type, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestParam) {
                return true;
            }
        }
        return false;
    }
}
