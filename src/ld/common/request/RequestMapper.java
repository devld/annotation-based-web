package ld.common.request;

import java.lang.reflect.Method;
import java.util.*;

public class RequestMapper {

    public static Map<Route, RequestCall> mapRequests(Set<Class<?>> classes) {
        Map<Route, RequestCall> requestCallMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getMethods();
            List<RequestCall> requestCalls = new LinkedList<>();
            String clazzPath = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping o = clazz.getAnnotation(RequestMapping.class);
                clazzPath = o.value();
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String methodPath = clazzPath + requestMapping.value();
                    // 将多个 / 换成 单个
                    methodPath = methodPath.replaceAll("/{2,}", "/");
                    Route route = new Route(methodPath, requestMapping.method());
                    RequestCall requestCall = new RequestCall(method);
                    if (requestCallMap.containsKey(route)) {
                        throw new IllegalArgumentException("RequestMapping [" + route + "] already exists.");
                    }
                    System.out.println("map " + route + " to " + clazz.getName() + "#" + method.getName());
                    requestCallMap.put(route, requestCall);
                    requestCalls.add(requestCall);
                }
            }
            if (!requestCalls.isEmpty()) {
                Object instance;
                try {
                    instance = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalStateException("cannot instance class: " + clazz, e);
                }
                for (RequestCall c : requestCalls) {
                    c.setInstance(instance);
                }
            }
        }
        return requestCallMap;
    }

}
