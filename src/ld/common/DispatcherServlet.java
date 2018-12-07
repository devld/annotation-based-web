package ld.common;

import ld.common.context.Context;
import ld.common.context.ContextImpl;
import ld.common.request.*;
import ld.common.request.param.PathVariableParamResolver;
import ld.common.request.param.RequestParamParamResolver;
import ld.common.response.HttpStatus;
import ld.common.response.HttpStatusResultWriter;
import ld.common.response.ResponseWriter;
import ld.common.response.ResponseWriterImpl;
import ld.common.response.thymeleaf.ThymeleafResultWriter;
import ld.common.util.ClassScanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

public class DispatcherServlet extends HttpServlet {

    private Map<Route, RequestCall> requestCallMap;
    private List<Route> routes;

    private List<ParameterResolver> parameterResolvers;
    private List<ResponseWriter> responseWriters;

    @Override
    public void init() throws ServletException {
        super.init();
        String requestPackage = getServletContext().getInitParameter("scanPackage");
        try {
            Set<Class<?>> classes = ClassScanner.findClasses(requestPackage);
            mapRequests(classes);
            addParameterResolvers(classes);
            addResponseWriter(classes);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException("mapping objects failed", e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String contextPath = getServletContext().getContextPath();
        String uri = req.getRequestURI().substring(contextPath.length());

        HttpMethod httpMethod;
        try {
            httpMethod = HttpMethod.valueOf(method.toUpperCase());
        } catch (Exception e) {
            resp.setStatus(HttpStatus.METHOD_NOT_ALLOWED.code());
            resp.getWriter().write("Cannot " + method + " " + uri + ": 405 Method Not Allowed");
            return;
        }

        Route matchedRoute = null;

        for (Route route : routes) {
            if (route.matched(httpMethod, uri)) {
                matchedRoute = route;
                break;
            }
        }

        if (matchedRoute == null) {
            resp.setStatus(HttpStatus.NOT_FOUND.code());
            resp.getWriter().write("Cannot " + method + " " + uri + ": 404 Not Found");
            return;
        }

        RequestCall requestCall = requestCallMap.get(matchedRoute);

        Context context = new ContextImpl(req, resp, getServletContext(), matchedRoute);

        Object[] params;
        try {
            params = resolveRequestParameters(requestCall, context);
        } catch (ParameterResolveException e) {
            resp.setStatus(HttpStatus.BAD_REQUEST.code());
            resp.getWriter().write(e.getMessage());
            return;
        }

        try {
            Object result = requestCall.call(params);
            Class<?> resultType = requestCall.getReturnType();

            writeResult(result, resultType, context);

        } catch (RequestCallException e) {
            Throwable ex = e.getCause();
            if (ex instanceof IOException) {
                throw (IOException) ex;
            }
            throw new ServletException(ex);
        }

    }

    private Object[] resolveRequestParameters(RequestCall requestCall, Context context) throws ParameterResolveException {
        Class<?>[] paramTypes = requestCall.getParamTypes();
        Annotation[][] paramAnnotations = requestCall.getParamAnnotations();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            Annotation[] annotations = paramAnnotations[i];
            boolean isOk = false;
            for (ParameterResolver resolver : parameterResolvers) {
                if (resolver.supported(type, annotations)) {
                    params[i] = resolver.resolveParameter(context, type, annotations);
                    isOk = true;
                    break;
                }
            }
            if (!isOk) {
                throw new IllegalArgumentException("cannot resolve parameter " + type.getName() + " at " + i);
            }
        }
        return params;
    }

    private void writeResult(Object result, Class<?> resultType, Context context) throws IOException {
        boolean isOk = false;

        for (ResponseWriter writer : responseWriters) {
            if (writer.supported(resultType)) {
                writer.write(result, resultType, context);
                isOk = true;
                break;
            }
        }

        if (!isOk) {
            throw new IllegalArgumentException("cannot write result " + resultType.getName());
        }
    }


    private void mapRequests(Set<Class<?>> classes) {
        requestCallMap = RequestMapper.mapRequests(classes);
        this.routes = new ArrayList<>();
        this.routes.addAll(requestCallMap.keySet());
        Collections.sort(this.routes);
    }

    private void addParameterResolvers(Set<Class<?>> classes) throws IllegalAccessException, InstantiationException {
        parameterResolvers = new LinkedList<>();

        parameterResolvers.add(new ContextParamResolver());
        parameterResolvers.add(new ContextUnwrapParamResolver());
        parameterResolvers.add(new PathVariableParamResolver());
        parameterResolvers.add(new RequestParamParamResolver());

        for (Class<?> c : classes) {
            if (ParameterResolver.class.isAssignableFrom(c) && !c.isInterface()) {
                parameterResolvers.add((ParameterResolver) c.newInstance());
            }
        }
    }

    private void addResponseWriter(Set<Class<?>> classes) throws IllegalAccessException, InstantiationException {
        responseWriters = new LinkedList<>();

        responseWriters.add(new HttpStatusResultWriter());
        responseWriters.add(new ThymeleafResultWriter(getServletContext()));

        for (Class<?> c : classes) {
            if (ResponseWriter.class.isAssignableFrom(c) && !c.isInterface()) {
                responseWriters.add((ResponseWriter) c.newInstance());
            }
        }

        responseWriters.add(new ResponseWriterImpl());
    }

}
