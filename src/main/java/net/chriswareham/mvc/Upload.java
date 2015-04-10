/*
 * @(#) Upload.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.mvc;

import java.io.InputStream;

/**
 * Interface to be implemented by classes that hold data for a file upload.
 *
 * @author Chris Wareham
 */
public interface Upload {
    /**
     * Get the filename of the upload.
     *
     * @return the filename of the upload
     */
    String getFilename();

    /**
     * Get the content type of the upload.
     *
     * @return the content type of the upload
     */
    String getContentType();

    /**
     * Get the size in bytes of the upload.
     *
     * @return the size in bytes of the upload
     */
    long getSize();

    /**
     * Get an input stream to read the data of the upload. Implementations of
     * this method must not require the caller to close the input stream.
     *
     * @return an input stream to read the data of the upload
     */
    InputStream getInputStream();

    /**
     * Get the data of the upload.
     *
     * @return the data of the upload
     */
    byte[] getData();
}
