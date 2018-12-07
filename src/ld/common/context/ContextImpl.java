package ld.common.context;

import ld.common.request.Route;
import ld.common.response.Model;
import ld.common.response.ModelImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextImpl implements Context {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext context;
    private final Route route;
    private final Model model;
    private final String requestUri;

    public ContextImpl(HttpServletRequest request, HttpServletResponse response, ServletContext context, Route route) {
        this.request = request;
        this.response = response;
        this.context = context;
        this.route = route;
        this.model = new ModelImpl();
        this.requestUri = getHttpServletRequest().getRequestURI().substring(getServletContext().getContextPath().length());
    }

    @Override
    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getHttpServletResponse() {
        return response;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public Route getRoute() {
        return route;
    }

    @Override
    public ServletContext getServletContext() {
        return context;
    }

    @Override
    public String getRequestUri() {
        return requestUri;
    }
}
