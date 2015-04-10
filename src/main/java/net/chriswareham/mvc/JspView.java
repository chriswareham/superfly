/*
 * @(#) JspView.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides a view that renders models as via a JSP.
 *
 * @author Chris Wareham
 */
public class JspView implements View {
    /**
     * The path to the JSP used to render models.
     */
    private String path;

    /**
     * Set the path to the JSP used to render models.
     *
     * @param p the path to the JSP used to render models
     */
    public void setPath(final String p) {
        path = p;
    }

    /**
     * Render models via a JSP. The models are exposed to the JSP as page
     * attributes.
     *
     * @param models the models
     * @param request the client request
     * @param response the servlet response
     * @param context the servlet context
     * @throws IOException if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void render(final Map<String, Object> models, final HttpServletRequest request, final HttpServletResponse response, final ServletContext context) throws IOException, ServletException {
        for (String key : models.keySet()) {
            request.setAttribute(key, models.get(key));
        }

        RequestDispatcher dispatcher = context.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
}
