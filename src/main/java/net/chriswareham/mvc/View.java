/*
 * @(#) View.java
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
 * This interface is implemented by classes that render models.
 *
 * @author Chris Wareham
 */
public interface View {
    /**
     * Render models.
     *
     * @param models the models
     * @param request the servlet request
     * @param response the servlet response
     * @param context the servlet context
     * @throws IOException if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    void render(Map<String, Object> models, HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException;
}
