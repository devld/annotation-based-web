package ld.common.response;

import ld.common.context.Context;

public class ResponseWriterImpl implements ResponseWriter {

    @Override
    public void write(Object result, Class<?> resultType, Context context) {
        // nothing
    }

    @Override
    public boolean supported(Class<?> resultType) {
        return true;
    }
}
