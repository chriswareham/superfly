/*
 * @(#) ActionResponse.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * Class that holds the view name and model data for a response.
 *
 * @author Chris Wareham
 */
public class ActionResponse {
    /**
     * The name of the view for the response.
     */
    private String viewName;
    /**
     * The data models for the response.
     */
    private final Map<String, Object> models = new HashMap<>();
    /**
     * The cookies for the response.
     */
    private final Map<String, Cookie> cookies = new HashMap<>();

    /**
     * Get the name of the view for the response.
     *
     * @return the name of the view for the response
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Set the name of the view for the response.
     *
     * @param vn the name of the view for the response
     */
    public void setViewName(final String vn) {
        viewName = vn;
    }

    /**
     * Get a data model for the response.
     *
     * @param name the name of the data model
     * @return the data model, or null if there isn't one
     */
    public Object getModel(final String name) {
        return models.get(name);
    }

    /**
     * Get the data models for the response.
     *
     * @return the data models for the response
     */
    public Map<String, Object> getModels() {
        return Collections.unmodifiableMap(models);
    }

    /**
     * Add a data model for the response.
     *
     * @param name the name of the data model
     * @param model the data model
     */
    public void addModel(final String name, final Object model) {
        models.put(name, model);
    }

    /**
     * Get a cookie for the response.
     *
     * @param name the name of the cookie
     * @return the cookie, or null if there isn't one
     */
    public Cookie getCookie(final String name) {
        return cookies.get(name);
    }

    /**
     * Get the cookies for the response.
     *
     * @return the cookies for the response
     */
    public Map<String, Cookie> getCookies() {
        return Collections.unmodifiableMap(cookies);
    }

    /**
     * Add a cookie for the response.
     *
     * @param cookie the cookie
     */
    public void addCookie(final Cookie cookie) {
        cookies.put(cookie.getName(), cookie);
    }
}
