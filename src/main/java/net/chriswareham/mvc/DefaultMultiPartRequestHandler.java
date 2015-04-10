/*
 * @(#) DefaultMultiPartRequestHandler.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.chriswareham.di.LifecycleComponent;

/**
 * This class implements a handler for multi-part requests.
 *
 * @author Chris Wareham
 */
public class DefaultMultiPartRequestHandler implements MultiPartRequestHandler, LifecycleComponent {
    /**
     * The file item factory.
     */
    private DiskFileItemFactory fileItemFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        fileItemFactory = new DiskFileItemFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        fileItemFactory = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMultiPartRequest(final HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseRequest(final HttpServletRequest request, final MutableActionRequest actionRequest) throws ActionException {
        try {
            ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);

            // The Commons file upload API is not type-safe
            @SuppressWarnings("unchecked") List<FileItem> items = fileUpload.parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString();
                    actionRequest.addParameter(name, value);
                } else {
                    String name = item.getFieldName();
                    DefaultUpload upload = new DefaultUpload();
                    upload.setFilename(item.getName());
                    upload.setContentType(item.getContentType());
                    upload.setSize(item.getSize());
                    upload.setData(item.get());
                    actionRequest.addAttribute(name, upload);
                }
            }
        } catch (FileUploadException exception) {
            throw new ActionException("Unable to parse request", exception);
        }
    }
}
