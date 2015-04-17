/*
 * @(#) JsonView.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides a view that renders models as JSON.
 *
 * @author Chris Wareham
 */
public class JsonView implements View {
    /**
     * The content type.
     */
    private static final String CONTENT_TYPE = "application/json";
    /**
     * The character encoding.
     */
    private static final String CHARACTER_ENCODING = "UTF-8";
    /**
     * The expires header.
     */
    private static final String EXPIRES_HEADER = "Expires";

    /**
     * The names of models to render as JSON.
     */
    private final List<String> modelNames = new ArrayList<>();
    /**
     * The optional expiry time in minutes.
     */
    private long expiry;

    /**
     * Add the name of a model to render as JSON.
     *
     * @param mn the name of a model to render as JSON.
     */
    public void addModelName(final String mn) {
        modelNames.add(mn);
    }

    /**
     * Set the optional expiry time in minutes.
     *
     * @param e the optional expiry time in minutes
     */
    public void setExpiry(final long e) {
        expiry = e;
    }

    /**
     * Render models as JSON.
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
        try {
            JSONObject obj = new JSONObject();

            for (String modelName : modelNames) {
                if (models.containsKey(modelName)) {
                    Object model = models.get(modelName);
                    if (model instanceof Collection<?>) {
                        obj.put(modelName, new JSONArray(model));
                    } else if (model instanceof Map<?, ?>) {
                        obj.put(modelName, model);
                    } else if (model instanceof String) {
                        obj.put(modelName, model);
                    } else {
                        obj.put(modelName, new JSONObject(model));
                    }
                }
            }

            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(CHARACTER_ENCODING);
            if (expiry > 0) {
                response.addDateHeader(EXPIRES_HEADER, expiry * 1000L + System.currentTimeMillis());
            }

            PrintWriter writer = response.getWriter();
            writer.print(obj.toString());
            writer.flush();
        } catch (JSONException exception) {
            throw new ServletException("Error serialising models as JSON", exception);
        }
    }
}
