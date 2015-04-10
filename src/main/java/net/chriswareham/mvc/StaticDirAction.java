/*
 * @(#) StaticFileAction.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.chriswareham.di.ClassPathResourceResolver;
import net.chriswareham.di.ResourceResolver;

/**
 * This class provides an action that serves static files from a directory on
 * the classpath.
 *
 * @author Chris Wareham
 */
public class StaticDirAction extends AbstractAction {
    /**
     * The default content type.
     */
    private static final String DEFAULT_CONTENT_TYPE = "text/plain";

    /**
     * The resource resolver.
     */
    private final ResourceResolver resolver = new ClassPathResourceResolver();
    /**
     * The content type mappings.
     */
    private final Map<String, String> contentTypes = new HashMap<>();
    /**
     * The name of the view.
     */
    private String viewName;

    /**
     * Add a content type mapping.
     *
     * @param e the file extension
     * @param ct the content type of the file
     */
    public void putContentType(final String e, final String ct) {
        contentTypes.put(e, ct);
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
     * Serve a static file from a directory on the classpath.
     *
     * @param request holds parameters and other data for the request
     * @return the view name and model data for the response
     * @throws ActionException if an error occurs
     */
    @Override
    public ActionResponse action(final ActionRequest request) throws ActionException {
        String path = request.getPath() + request.getPathInfo();

        InputStream in = resolver.getResource(path);

        if (in == null) {
            throw new ResourceNotFoundException("Invalid resource " + path);
        }

        String contentType = getContentType(path);

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

    /**
     * Get the most appropriate content type for a file bases on extension.
     *
     * @param path the file path
     * @return the most appropriate content type for the file
     */
    private String getContentType(final String path) {
        String contentType = null;

        int i = path.lastIndexOf('.') + 1;
        if (i > 0) {
            contentType = contentTypes.get(path.substring(i));
        }

        return contentType != null ? contentType : DEFAULT_CONTENT_TYPE;
    }
}
