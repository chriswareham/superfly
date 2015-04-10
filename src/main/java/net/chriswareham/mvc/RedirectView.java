/*
 * @(#) RedirectView.java
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

import net.chriswareham.util.Strings;

/**
 * This class provides a view that sends redirects.
 *
 * @author Chris Wareham
 */
public class RedirectView implements View {
    /**
     * The location header.
     */
    private static final String LOCATION_HEADER = "Location";

    /**
     * The name of the redirect model.
     */
    private String redirectModel;
    /**
     * Whether to send a permanent redirect (HTTP Status Code 301).
     */
    private boolean permanentRedirect;
    /**
     * Whether to persist a query string from the original request.
     */
    private boolean persistQueryString;

    /**
     * Set the name of the redirect model.
     *
     * @param rm the name of the redirect model
     */
    public void setRedirectModel(final String rm) {
        redirectModel = rm;
    }

    /**
     * Set whether to send a permanent redirect (HTTP Status Code 301).
     *
     * @param pr whether to send a permanent redirect
     */
    public void setPermanentRedirect(final boolean pr) {
        permanentRedirect = pr;
    }

    /**
     * Set whether to persist a query string from the original request.
     *
     * @param pqs whether to persist a query string from the original request
     */
    public void setPersistQueryString(final boolean pqs) {
        persistQueryString = pqs;
    }

    /**
     * Send a redirect. The models must includes a redirect entry, the string
     * representation of which will be used as the redirect URL. If the redirect
     * URL is not absolute then the servlet context path will be prepended to
     * it.
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
        if (!models.containsKey(redirectModel)) {
            throw new IllegalStateException("No redirect model specified");
        }

        String redirect = models.get(redirectModel).toString();
        if (redirect.charAt(0) != '/') {
            redirect = request.getContextPath() + redirect;
        }

        if (persistQueryString && !Strings.isNullOrEmpty(request.getQueryString())) {
            redirect += "?" + request.getQueryString();
        }

        if (permanentRedirect) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader(LOCATION_HEADER, redirect);
        } else {
            response.sendRedirect(redirect);
        }
    }
}
