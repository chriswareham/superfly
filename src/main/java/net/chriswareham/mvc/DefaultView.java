/*
 * @(#) DefaultView.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides a default view.
 *
 * @author Chris Wareham
 */
public class DefaultView implements View {
    /**
     * Default render.
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
        return;
    }
}
