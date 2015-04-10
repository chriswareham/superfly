/*
 * @(#) RequestProcessor.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface is implemented by classes that handle requests in the MVC
 * framework.
 *
 * @author Chris Wareham
 */
public interface RequestProcessor {
    /**
     * Process a request.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param context the servlet context
     * @throws IOException if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    void process(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException;
}
