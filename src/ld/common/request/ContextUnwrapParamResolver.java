package ld.common.request;

import ld.common.context.Context;
import ld.common.response.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

public class ContextUnwrapParamResolver implements ParameterResolver {
    @Override
    public Object resolveParameter(Context context, Class<?> type, Annotation[] annotations) {
        if (type == Model.class) {
            return context.getModel();
        } else if (type == HttpServletRequest.class) {
            return context.getHttpServletRequest();
        } else if (type == HttpServletResponse.class) {
            return context.getHttpServletResponse();
        } else if (type == Route.class) {
            return context.getRoute();
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean supported(Class<?> type, Annotation[] annotations) {
        return type == Model.class || type == HttpServletRequest.class || type == HttpServletResponse.class || type == Route.class;
    }
}
