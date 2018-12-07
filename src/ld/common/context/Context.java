package ld.common.context;

import ld.common.request.Route;
import ld.common.response.Model;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Context {

    HttpServletRequest getHttpServletRequest();

    HttpServletResponse getHttpServletResponse();

    Model getModel();

    Route getRoute();

    ServletContext getServletContext();

    String getRequestUri();

}
