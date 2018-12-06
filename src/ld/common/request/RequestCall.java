package ld.common.request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestCall {

    private Method method;
    private Object instance;
    private Class<?>[] paramTypes;
    private Annotation[][] paramAnnotations;

    RequestCall(Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException();
        }
        this.paramAnnotations = method.getParameterAnnotations();
        this.paramTypes = method.getParameterTypes();
        this.method = method;
    }

    void setInstance(Object instance) {
        this.instance = instance;
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public Annotation[][] getParamAnnotations() {
        return paramAnnotations;
    }

    public Object call(Object... params) {
        if (instance == null) {
            throw new IllegalStateException("must call setInstance first");
        }
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new RequestCallException(e.getTargetException());
        }
    }

}
