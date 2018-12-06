package ld.common.response;

import ld.common.context.Context;

public class HttpStatusResultWriter implements ResponseWriter {

    @Override
    public void write(Object result, Class<?> resultType, Context context) {
        HttpStatus status = (HttpStatus) result;
        context.getHttpServletResponse().setStatus(status.code());
    }

    @Override
    public boolean supported(Class<?> resultType) {
        return HttpStatus.class.isAssignableFrom(resultType);
    }
}
