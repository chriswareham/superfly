/*
 * @(#) StaticFileAction.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.chriswareham.di.ClassPathResourceResolver;
import net.chriswareham.di.ResourceResolver;

/**
 * This class provides an action that serves static files from the classpath.
 *
 * @author Chris Wareham
 */
public class StaticFileAction extends AbstractAction {
    /**
     * The resource resolver.
     */
    private final ResourceResolver resolver = new ClassPathResourceResolver();
    /**
     * The content type of the file.
     */
    private String contentType;
    /**
     * The name of the view.
     */
    private String viewName;

    /**
     * Set the content type of the file.
     *
     * @param ct the content type of the file
     */
    public void setContentType(final String ct) {
        contentType = ct;
    }

    /**
     * Set the name of the view.
     *
     * @param vn the name of the view
     */
    public void setViewName(final String vn) {
        viewName = vn;
    }

    /**
     * Serve a static file from the classpath.
     *
     * @param request holds parameters and other data for the request
     * @return the view name and model data for the response
     * @throws ActionException if an error occurs
     */
    @Override
    public ActionResponse action(final ActionRequest request) throws ActionException {
        String path = request.getPath();

        InputStream in = resolver.getResource(path);

        if (in == null) {
            throw new ResourceNotFoundException("Invalid resource " + path);
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                out.write(buf, 0, n);
            }

            ActionResponse response = new ActionResponse();
            response.setViewName(viewName);
            response.addModel("data", out.toByteArray());
            response.addModel("content_type", contentType);
            return response;
        } catch (IOException exception) {
            throw new ActionException("Failed to read resource " + path, exception);
        } finally {
            try {
                in.close();
            } catch (IOException exception) {
                in = null;
            }
        }
    }
}
