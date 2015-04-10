/*
 * @(#) DefaultUpload.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Class that holds data for a file upload.
 *
 * @author Chris Wareham
 */
public class DefaultUpload implements Upload {
    /**
     * The default MIME type.
     */
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    /**
     * The filename of the upload.
     */
    private String filename;
    /**
     * The content type of the upload.
     */
    private String contentType;
    /**
     * The size in bytes of the upload.
     */
    private long size;
    /**
     * The data of the upload.
     */
    private byte[] data;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename of the upload.
     *
     * @param f the filename of the upload
     */
    public void setFilename(final String f) {
        filename = f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType() {
        return contentType != null ? contentType : DEFAULT_MIME_TYPE;
    }

    /**
     * Set the content type of the upload.
     *
     * @param ct the content type of the upload
     */
    public void setContentType(final String ct) {
        contentType = ct;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSize() {
        return size;
    }

    /**
     * Set the size in bytes of the upload.
     *
     * @param s the size in bytes of the upload
     */
    public void setSize(final long s) {
        size = s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getData() {
        return data;
    }

    /**
     * Set the data of the upload.
     *
     * @param d the data of the upload
     */
    public void setData(final byte[] d) {
        data = d;
    }
}
