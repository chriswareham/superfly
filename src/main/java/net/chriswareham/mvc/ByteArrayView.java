/*
 * @(#) ByteArrayView.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides a view that renders models as a byte stream.
 *
 * @author Chris Wareham
 */
public class ByteArrayView implements View {
    /**
     * The if modified since header.
     */
    private static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
    /**
     * The last modified header.
     */
    private static final String LAST_MODIFIED_HEADER = "Last-Modified";

    /**
     * The name of the array model.
     */
    private String arrayModel;
    /**
     * The name of the content type model.
     */
    private String contentTypeModel;
    /**
     * The name of the last modified model.
     */
    private String lastModifiedModel;

    /**
     * Set the name of the array model.
     *
     * @param am the name of the array model
     */
    public void setArrayModel(final String am) {
        arrayModel = am;
    }

    /**
     * Set the name of the content type model.
     *
     * @param ctm the name of the content type model
     */
    public void setContentTypeModel(final String ctm) {
        contentTypeModel = ctm;
    }

    /**
     * Set the name of the last modified model.
     *
     * @param lmm the name of the last modified model
     */
    public void setLastModifiedModel(final String lmm) {
        lastModifiedModel = lmm;
    }

    /**
     * Render models as a byte stream. The models must include an entry of type
     * <code>byte[]</code>, which provides the data to stream. The models may
     * include a content type entry, the string representation of which will be
     * used as the content type of the data. The models may also include a last
     * modified entry, the string representation of which must be parsable as a
     * long integer that will be used as the modification timestamp of the data.
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
        if (lastModifiedModel != null && models.containsKey(lastModifiedModel)) {
            long lastModified = Long.parseLong(models.get(lastModifiedModel).toString());
            long ifModifiedSince = request.getDateHeader(IF_MODIFIED_SINCE_HEADER);
            if (ifModifiedSince > lastModified) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
            response.setDateHeader(LAST_MODIFIED_HEADER, lastModified);
        }

        if (!models.containsKey(arrayModel)) {
            throw new IllegalStateException("Array model not specified");
        }

        if (!(models.get(arrayModel) instanceof byte[])) {
            throw new IllegalStateException("Array model not instance of byte array");
        }

        byte[] bytes = (byte[]) models.get(arrayModel);

        response.setContentLength(bytes.length);

        if (contentTypeModel != null && models.containsKey(contentTypeModel)) {
            String contentType = models.get(contentTypeModel).toString();
            response.setContentType(contentType);
        }

        try {
            OutputStream out = response.getOutputStream();
            out.write(bytes, 0, bytes.length);
            out.flush();
        } catch (IOException exception) {
            // assume the client aborted the request (the following line just keeps Checkstyle happy)
            return;
        }
    }
}
